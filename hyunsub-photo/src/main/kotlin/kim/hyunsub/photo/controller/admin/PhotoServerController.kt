package kim.hyunsub.photo.controller.admin

import kim.hyunsub.common.config.AppConstants
import kim.hyunsub.common.fs.model.UserDeleteParams
import kim.hyunsub.common.fs.model.UserInitParams
import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.model.SimpleResponse
import kim.hyunsub.photo.repository.condition.AlbumCondition
import kim.hyunsub.photo.repository.condition.AlbumOwnerCondition
import kim.hyunsub.photo.repository.condition.AlbumPhotoCondition
import kim.hyunsub.photo.repository.condition.PhotoOwnerCondition
import kim.hyunsub.photo.repository.entity.Album
import kim.hyunsub.photo.repository.entity.AlbumOwner
import kim.hyunsub.photo.repository.entity.AlbumPhoto
import kim.hyunsub.photo.repository.entity.PhotoOwner
import kim.hyunsub.photo.repository.mapper.AlbumMapper
import kim.hyunsub.photo.repository.mapper.AlbumOwnerMapper
import kim.hyunsub.photo.repository.mapper.AlbumPhotoMapper
import kim.hyunsub.photo.repository.mapper.PhotoOwnerMapper
import kim.hyunsub.photo.repository.mapper.generateId
import mu.KotlinLogging
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@Authorized(["server"])
@RestController
@RequestMapping("/api/v1/server")
class PhotoServerController(
	private val albumMapper: AlbumMapper,
	private val albumOwnerMapper: AlbumOwnerMapper,
	private val photoOwnerMapper: PhotoOwnerMapper,
	private val albumPhotoMapper: AlbumPhotoMapper,
) {
	private val log = KotlinLogging.logger { }

	@PostMapping("/user/init")
	fun userInit(@RequestBody params: UserInitParams): SimpleResponse {
		log.debug { "[User Init] params=$params" }

		val fromUserId = AppConstants.INIT_FROM_USER_ID
		val toUserId = params.userId

		val albums = albumMapper.select(AlbumCondition(userId = fromUserId))
		val albumOwners = albumOwnerMapper.select(AlbumOwnerCondition(userId = fromUserId))
		val albumPhotos = albumPhotoMapper.select(AlbumPhotoCondition(albumIds = albums.map { it.id }))
		val photoOwners = photoOwnerMapper.select(PhotoOwnerCondition(userId = fromUserId))

		log.debug { "[User Init] albums=$albums" }
		log.debug { "[User Init] albumOwners=$albumOwners" }
		log.debug { "[User Init] albumPhotos=$albumPhotos" }
		log.debug { "[User Init] photoOwners=$photoOwners" }

		val newAlbums = mutableListOf<Album>()
		val newAlbumOwners = mutableListOf<AlbumOwner>()
		val newAlbumPhotos = mutableListOf<AlbumPhoto>()
		val newPhotoOwners = mutableListOf<PhotoOwner>()

		for (album in albums) {
			val newAlbum = album.copy(
				id = albumMapper.generateId(),
				regDt = LocalDateTime.now(),
			)
			newAlbums.add(newAlbum)

			newAlbumOwners.add(
				albumOwners.first { it.albumId == album.id }
					.copy(albumId = newAlbum.id, userId = toUserId)
			)

			newAlbumPhotos.addAll(
				albumPhotos.filter { it.albumId == album.id }
					.map { it.copy(albumId = newAlbum.id, userId = toUserId) }
			)

			newPhotoOwners.addAll(
				photoOwners.filter { it.userId == fromUserId }
					.map { it.copy(userId = toUserId, regDt = LocalDateTime.now()) }
			)
		}

		log.debug { "[User Init] newAlbums=$newAlbums" }
		log.debug { "[User Init] newAlbumOwners=$newAlbumOwners" }
		log.debug { "[User Init] newAlbumPhotos=$newAlbumPhotos" }
		log.debug { "[User Init] newPhotoOwners=$newPhotoOwners" }

		if (!params.dryRun) {
			albumMapper.insertAll(newAlbums)
			albumOwnerMapper.insertAll(newAlbumOwners)
			albumPhotoMapper.insertAll(newAlbumPhotos)
			photoOwnerMapper.insertAll(newPhotoOwners)
		}

		return SimpleResponse()
	}

	@PostMapping("/user/delete")
	fun userDelete(@RequestBody params: UserDeleteParams): SimpleResponse {
		log.debug { "[User Delete] params=$params" }

		val userId = params.userId

		val albums = albumMapper.select(AlbumCondition(userId = userId))
		val albumOwners = albumOwnerMapper.select(AlbumOwnerCondition(userId = userId))
		val albumPhotos = albumPhotoMapper.select(AlbumPhotoCondition(albumIds = albums.map { it.id }))
		val photoOwners = photoOwnerMapper.select(PhotoOwnerCondition(userId = userId))

		log.debug { "[User Delete] albums=$albums" }
		log.debug { "[User Delete] albumOwners=$albumOwners" }
		log.debug { "[User Delete] albumPhotos=$albumPhotos" }
		log.debug { "[User Delete] photoOwners=$photoOwners" }

		if (!params.dryRun) {
			albumMapper.deleteByIds(albums.map { it.id })
			albumOwnerMapper.deleteAll(albumOwners)
			albumPhotoMapper.deleteAll(albumPhotos)
			photoOwnerMapper.deleteAll(photoOwners)
		}

		return SimpleResponse()
	}
}
