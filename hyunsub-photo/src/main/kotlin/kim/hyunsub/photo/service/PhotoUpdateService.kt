package kim.hyunsub.photo.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.transaction.Transactional
import kim.hyunsub.common.fs.client.FsClient
import kim.hyunsub.common.fs.client.rename
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.photo.model.PhotoDateType
import kim.hyunsub.photo.model.api.ApiRescanDateResult
import kim.hyunsub.photo.repository.condition.AlbumCondition
import kim.hyunsub.photo.repository.entity.Photo
import kim.hyunsub.photo.repository.mapper.AlbumMapper
import kim.hyunsub.photo.repository.mapper.AlbumPhotoMapper
import kim.hyunsub.photo.repository.mapper.PhotoMapper
import kim.hyunsub.photo.repository.mapper.PhotoMetadataMapper
import kim.hyunsub.photo.repository.mapper.PhotoOwnerMapper
import kim.hyunsub.photo.repository.mapper.generateId
import kim.hyunsub.photo.util.PhotoDateParser
import kim.hyunsub.photo.util.PhotoPathConverter
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class PhotoUpdateService(
	private val fsClient: FsClient,
	private val photoOwnerMapper: PhotoOwnerMapper,
	private val albumMapper: AlbumMapper,
	private val photoMetadataMapper: PhotoMetadataMapper,
	private val photoMapper: PhotoMapper,
	private val albumPhotoMapper: AlbumPhotoMapper,
) {
	private val log = KotlinLogging.logger { }
	private val mapper = jacksonObjectMapper()

	@Transactional
	fun updateId(photo: Photo, newId: String): Photo {
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

		val albums = albumMapper.select(AlbumCondition(thumbnailPhotoId = oldId))
		for (album in albums) {
			val newAlbum = album.copy(thumbnailPhotoId = newId)
			albumMapper.insert(newAlbum)
		}

		val photoResult = photoMapper.updateId(from = oldId, to = newId)
		val photoOwner = photoOwnerMapper.updatePhotoId(oldId, newId)
		val albumPhoto = albumPhotoMapper.updatePhotoId(oldId, newId)
		val photoMetadata = photoMetadataMapper.updatePhotoId(oldId, newId)

		log.debug { "[Update Photo Id] albums=${albums.size} photo=$photoResult, photoOwner=$photoOwner, albumPhoto=$albumPhoto, photoMetadata=$photoMetadata" }

		return newPhoto
	}

	@Transactional
	fun rescanPhotoDate(photoId: String): ApiRescanDateResult? {
		val photo = photoMapper.selectOne(photoId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val metadata = photoMetadataMapper.selectOne(photo.id)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val exif = mapper.readTree(metadata.raw)

		val result = PhotoDateParser.parse(exif, "", System.currentTimeMillis())
		if (result.type != PhotoDateType.EXIF) {
			return null
		}

		if (result.date == photo.date) {
			return null
		}

		val newId = photoMapper.generateId(result.date, photo.hash)
		log.debug { "[Rescan Photo Date] ${photo.id} -> $newId : ${photo.date} -> ${result.date}" }
		updateId(photo, newId)

		val newPhoto = photo.copy(
			id = newId,
			dateType = PhotoDateType.EXIF,
			offset = result.date.offset.totalSeconds,
		)
		photoMapper.insert(newPhoto)

		return ApiRescanDateResult(
			oldId = photoId,
			newId = newId,
			oldDate = photo.date,
			newDate = result.date,
		)
	}
}
