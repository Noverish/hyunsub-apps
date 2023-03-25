package kim.hyunsub.photo.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kim.hyunsub.common.api.ApiCaller
import kim.hyunsub.common.util.decodeHex
import kim.hyunsub.common.util.toBase64
import kim.hyunsub.photo.model.dto.PhotoUploadParams
import kim.hyunsub.photo.repository.AlbumPhotoRepository
import kim.hyunsub.photo.repository.AlbumV2Repository
import kim.hyunsub.photo.repository.PhotoMetadataV2Repository
import kim.hyunsub.photo.repository.PhotoOwnerRepository
import kim.hyunsub.photo.repository.PhotoV2Repository
import kim.hyunsub.photo.repository.entity.AlbumPhoto
import kim.hyunsub.photo.repository.entity.AlbumPhotoId
import kim.hyunsub.photo.repository.entity.PhotoMetadataV2
import kim.hyunsub.photo.repository.entity.PhotoOwner
import kim.hyunsub.photo.repository.entity.PhotoOwnerId
import kim.hyunsub.photo.repository.entity.PhotoV2
import kim.hyunsub.photo.repository.generateId
import kim.hyunsub.photo.util.PhotoDateParser
import kim.hyunsub.photo.util.PhotoPathUtils
import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import kotlin.io.path.Path
import kotlin.io.path.extension

@Service
class PhotoUploadService(
	private val apiCaller: ApiCaller,
	private val encodeApiCaller: PhotoEncodeApiCaller,
	private val photoV2Repository: PhotoV2Repository,
	private val thumbnailServiceV2: ThumbnailServiceV2,
	private val photoOwnerRepository: PhotoOwnerRepository,
	private val albumPhotoRepository: AlbumPhotoRepository,
	private val albumRepository: AlbumV2Repository,
	private val photoMetadataRepository: PhotoMetadataV2Repository,
) {
	private val log = KotlinLogging.logger { }
	private val mapper = jacksonObjectMapper()

	fun upload(userId: String, params: PhotoUploadParams): PhotoV2 {
		val photo = getOrCreatePhoto(params)

		getOrCreatePhotoOwner(userId, photo, params)

		params.albumId?.let {
			getOrCreateAlbumPhoto(userId, photo, it)
		}

		return photo
	}

	private fun getOrCreatePhoto(params: PhotoUploadParams): PhotoV2 {
		val tmpPath = PhotoPathUtils.tmp(params.nonce)

		val hash = apiCaller.hash(tmpPath).result.decodeHex().toBase64()

		val exist = photoV2Repository.findByHash(hash)
		if (exist != null) {
			log.debug { "[PhotoUpload] Already exist photo: $exist" }
			apiCaller.remove(tmpPath)
			return exist
		}

		val exif = mapper.readTree(apiCaller.exif(tmpPath))[0]
		val date = PhotoDateParser.parse(exif, params.name)
		val id = photoV2Repository.generateId(date, hash)

		val photo = PhotoV2(
			id = id,
			hash = hash,
			width = exif["ImageWidth"].asInt(),
			height = exif["ImageHeight"].asInt(),
			size = exif["FileSize"].asInt(),
			offset = date.offset.totalSeconds,
			ext = Path(params.name).extension,
		)

		// move photo to original folder
		val originalPath = PhotoPathUtils.original(photo)
		apiCaller.rename(tmpPath, originalPath)

		// generate thumbnail
		thumbnailServiceV2.generateThumbnail(photo)
		if (photo.isVideo) {
			val videoPath = PhotoPathUtils.video(id)
			encodeApiCaller.encode(
				input = originalPath,
				output = videoPath,
				photoId = id,
			)
		}

		// save photo
		photoV2Repository.save(photo)

		// save metadata
		photoMetadataRepository.save(PhotoMetadataV2.from(id, exif))

		return photo
	}

	private fun getOrCreatePhotoOwner(userId: String, photo: PhotoV2, params: PhotoUploadParams): PhotoOwner {
		val exist = photoOwnerRepository.findByIdOrNull(PhotoOwnerId(userId, photo.id))
		if (exist != null) {
			log.debug { "[PhotoUpload] Already exist photo owner: $exist" }
			return exist
		}

		val photoOwner = PhotoOwner(
			userId = userId,
			photoId = photo.id,
			name = params.name,
			regDt = LocalDateTime.now(),
		)
		photoOwnerRepository.save(photoOwner)

		return photoOwner
	}

	private fun getOrCreateAlbumPhoto(userId: String, photo: PhotoV2, albumId: String): AlbumPhoto {
		val exist = albumPhotoRepository.findByIdOrNull(AlbumPhotoId(albumId, photo.id))
		if (exist != null) {
			log.debug { "[PhotoUpload] Already exist album photo: $exist" }
			return exist
		}

		val albumPhoto = AlbumPhoto(
			albumId = albumId,
			photoId = photo.id,
			userId = userId,
		)
		albumPhotoRepository.save(albumPhoto)

		val album = albumRepository.findByIdOrNull(albumId)
		if (album != null && album.thumbnailPhotoId == null) {
			albumRepository.save(album.copy(thumbnailPhotoId = photo.id))
		}

		return albumPhoto
	}
}
