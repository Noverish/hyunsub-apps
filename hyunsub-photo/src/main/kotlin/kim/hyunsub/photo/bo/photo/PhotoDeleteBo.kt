package kim.hyunsub.photo.bo.photo

import kim.hyunsub.common.fs.client.FsClient
import kim.hyunsub.common.fs.client.remove
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.photo.model.api.ApiPhoto
import kim.hyunsub.photo.model.api.toApi
import kim.hyunsub.photo.model.dto.PhotoDeleteBulkParams
import kim.hyunsub.photo.repository.condition.AlbumPhotoCondition
import kim.hyunsub.photo.repository.condition.PhotoOwnerCondition
import kim.hyunsub.photo.repository.entity.Photo
import kim.hyunsub.photo.repository.mapper.AlbumPhotoMapper
import kim.hyunsub.photo.repository.mapper.PhotoMapper
import kim.hyunsub.photo.repository.mapper.PhotoOwnerMapper
import kim.hyunsub.photo.service.AlbumThumbnailService
import kim.hyunsub.photo.util.PhotoPathConverter
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class PhotoDeleteBo(
	private val fsClient: FsClient,
	private val photoMapper: PhotoMapper,
	private val photoOwnerMapper: PhotoOwnerMapper,
	private val albumThumbnailService: AlbumThumbnailService,
	private val albumPhotoMapper: AlbumPhotoMapper,
) {
	private val log = KotlinLogging.logger { }

	fun deleteBulk(userId: String, params: PhotoDeleteBulkParams): List<ApiPhoto> {
		return params.photoIds.map { delete(userId, it) }
	}

	fun delete(userId: String, photoId: String): ApiPhoto {
		val photoOwner = photoOwnerMapper.selectOne(userId, photoId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such photo owner")

		val photo = photoMapper.selectOne(photoId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such photo")

		log.debug { "[Delete Photo] Delete photo owner: $photoOwner" }
		photoOwnerMapper.delete(photoOwner)

		val albumPhotos = albumPhotoMapper.select(AlbumPhotoCondition(userId = userId, photoId = photoId))
		log.debug { "[Delete Photo] Delete album photos: $albumPhotos" }
		albumPhotoMapper.deleteAll(albumPhotos)

		albumThumbnailService.delete(photoId)

		val numberOfOwner = photoOwnerMapper.count(PhotoOwnerCondition(photoId = photoId))
		if (numberOfOwner == 0) {
			log.debug { "[Delete Photo] Delete photo file: $photo" }
			deleteFile(photo)
		}

		return photo.toApi(photoOwner)
	}

	private fun deleteFile(photo: Photo) {
		val photoId = photo.id
		val originalPath = PhotoPathConverter.original(photo)
		val thumbnailPath = PhotoPathConverter.thumbnail(photoId)

		fsClient.remove(originalPath)
		fsClient.remove(thumbnailPath)

		if (photo.isVideo) {
			val videoPath = PhotoPathConverter.video(photoId)
			fsClient.remove(videoPath)
		}

		photoMapper.deleteById(photoId)
	}
}
