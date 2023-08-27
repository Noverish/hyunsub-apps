package kim.hyunsub.photo.controller

import kim.hyunsub.common.config.AppConstants
import kim.hyunsub.common.fs.model.UserDeleteParams
import kim.hyunsub.common.fs.model.UserInitParams
import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.model.SimpleResponse2
import kim.hyunsub.photo.repository.AlbumOwnerRepository
import kim.hyunsub.photo.repository.AlbumPhotoRepository
import kim.hyunsub.photo.repository.AlbumRepository
import kim.hyunsub.photo.repository.PhotoOwnerRepository
import kim.hyunsub.photo.repository.entity.Album
import kim.hyunsub.photo.repository.entity.AlbumOwner
import kim.hyunsub.photo.repository.entity.AlbumPhoto
import kim.hyunsub.photo.repository.entity.PhotoOwner
import kim.hyunsub.photo.repository.generateId
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
	private val albumRepository: AlbumRepository,
	private val albumOwnerRepository: AlbumOwnerRepository,
	private val albumPhotoRepository: AlbumPhotoRepository,
	private val photoOwnerRepository: PhotoOwnerRepository,
) {
	private val log = KotlinLogging.logger { }

	@PostMapping("/user/init")
	fun userInit(@RequestBody params: UserInitParams): SimpleResponse2 {
		log.debug { "[User Init] params=$params" }

		val fromUserId = AppConstants.INIT_FROM_USER_ID
		val toUserId = params.userId

		val albums = albumRepository.findByUserId(fromUserId)
		val albumOwners = albumOwnerRepository.findByUserId(fromUserId)
		val albumPhotos = albumPhotoRepository.findByAlbumIdIn(albums.map { it.id })
		val photoOwners = photoOwnerRepository.findByUserId(fromUserId)

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
				id = albumRepository.generateId(),
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
			albumRepository.saveAll(newAlbums)
			albumOwnerRepository.saveAll(newAlbumOwners)
			albumPhotoRepository.saveAll(newAlbumPhotos)
			photoOwnerRepository.saveAll(newPhotoOwners)
		}

		return SimpleResponse2()
	}

	@PostMapping("/user/delete")
	fun userDelete(@RequestBody params: UserDeleteParams): SimpleResponse2 {
		log.debug { "[User Delete] params=$params" }

		val userId = params.userId

		val albums = albumRepository.findByUserId(userId)
		val albumOwners = albumOwnerRepository.findByUserId(userId)
		val albumPhotos = albumPhotoRepository.findByAlbumIdIn(albums.map { it.id })
		val photoOwners = photoOwnerRepository.findByUserId(userId)

		log.debug { "[User Delete] albums=$albums" }
		log.debug { "[User Delete] albumOwners=$albumOwners" }
		log.debug { "[User Delete] albumPhotos=$albumPhotos" }
		log.debug { "[User Delete] photoOwners=$photoOwners" }

		if (!params.dryRun) {
			albumRepository.deleteAll(albums)
			albumOwnerRepository.deleteAll(albumOwners)
			albumPhotoRepository.deleteAll(albumPhotos)
			photoOwnerRepository.deleteAll(photoOwners)
		}

		return SimpleResponse2()
	}
}
