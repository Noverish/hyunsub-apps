package kim.hyunsub.photo.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kim.hyunsub.common.fs.client.FsClient
import kim.hyunsub.common.fs.client.rename
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.photo.model.PhotoDateType
import kim.hyunsub.photo.model.api.ApiRescanDateResult
import kim.hyunsub.photo.repository.AlbumPhotoRepository
import kim.hyunsub.photo.repository.AlbumRepository
import kim.hyunsub.photo.repository.PhotoMetadataRepository
import kim.hyunsub.photo.repository.PhotoOwnerRepository
import kim.hyunsub.photo.repository.PhotoRepository
import kim.hyunsub.photo.repository.entity.Photo
import kim.hyunsub.photo.repository.generateId
import kim.hyunsub.photo.util.PhotoDateParser
import kim.hyunsub.photo.util.PhotoPathConverter
import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class PhotoUpdateService(
	private val fsClient: FsClient,
	private val photoRepository: PhotoRepository,
	private val photoOwnerRepository: PhotoOwnerRepository,
	private val albumPhotoRepository: AlbumPhotoRepository,
	private val albumRepository: AlbumRepository,
	private val photoMetadataRepository: PhotoMetadataRepository,
) {
	private val log = KotlinLogging.logger { }
	private val mapper = jacksonObjectMapper()

	@Transactional
	fun updateId(photo: Photo, newId: String) {
		val oldId = photo.id
		val newPhoto = photo.copy(id = newId)

		val oldOriginalPath = PhotoPathConverter.original(photo)
		val newOriginalPath = PhotoPathConverter.original(newPhoto)
		fsClient.rename(oldOriginalPath, newOriginalPath)

		val oldThumbnailPath = PhotoPathConverter.thumbnail(oldId)
		val newThumbnailPath = PhotoPathConverter.thumbnail(newId)
		fsClient.rename(oldThumbnailPath, newThumbnailPath)

		if (photo.isVideo) {
			val oldVideoPath = PhotoPathConverter.video(oldId)
			val newVideoPath = PhotoPathConverter.video(newId)
			fsClient.rename(oldVideoPath, newVideoPath)
		}

		val albums = albumRepository.findByThumbnailPhotoId(oldId)
		for (album in albums) {
			val newAlbum = album.copy(thumbnailPhotoId = newId)
			albumRepository.save(newAlbum)
		}

		val photoResult = photoRepository.updateId(oldId, newId)
		val photoOwner = photoOwnerRepository.updatePhotoId(oldId, newId)
		val albumPhoto = albumPhotoRepository.updatePhotoId(oldId, newId)
		val photoMetadata = photoMetadataRepository.updatePhotoId(oldId, newId)

		log.debug { "[Update Photo Id] albums=${albums.size} photo=$photoResult, photoOwner=$photoOwner, albumPhoto=$albumPhoto, photoMetadata=$photoMetadata" }
	}

	@Transactional
	fun rescanPhotoDate(photoId: String): ApiRescanDateResult? {
		val photo = photoRepository.findByIdOrNull(photoId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val metadata = photoMetadataRepository.findByIdOrNull(photo.id)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val exif = mapper.readTree(metadata.raw)

		val result = PhotoDateParser.parse(exif, "", System.currentTimeMillis())
		if (result.type != PhotoDateType.EXIF) {
			return null
		}

		if (result.date == photo.date) {
			return null
		}

		val newId = photoRepository.generateId(result.date, photo.hash)
		log.debug { "[Rescan Photo Date] ${photo.id} -> $newId : ${photo.date} -> ${result.date}" }
		updateId(photo, newId)

		val newPhoto = photo.copy(
			id = newId,
			dateType = PhotoDateType.EXIF,
			offset = result.date.offset.totalSeconds,
		)
		photoRepository.save(newPhoto)

		return ApiRescanDateResult(
			oldId = photoId,
			newId = newId,
			oldDate = photo.date,
			newDate = result.date,
		)
	}
}
