package kim.hyunsub.photo.bo

import kim.hyunsub.common.fs.client.FsClient
import kim.hyunsub.common.fs.client.remove
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.photo.model.api.ApiPhoto
import kim.hyunsub.photo.model.api.toApi
import kim.hyunsub.photo.model.dto.PhotoDeleteBulkParams
import kim.hyunsub.photo.repository.AlbumPhotoRepository
import kim.hyunsub.photo.repository.PhotoOwnerRepository
import kim.hyunsub.photo.repository.PhotoRepository
import kim.hyunsub.photo.repository.entity.Photo
import kim.hyunsub.photo.repository.entity.PhotoOwnerId
import kim.hyunsub.photo.util.PhotoPathConverter
import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class PhotoDeleteBo(
	private val fsClient: FsClient,
	private val photoRepository: PhotoRepository,
	private val photoOwnerRepository: PhotoOwnerRepository,
	private val albumPhotoRepository: AlbumPhotoRepository,
) {
	private val log = KotlinLogging.logger { }

	fun deleteBulk(userId: String, params: PhotoDeleteBulkParams): List<ApiPhoto> {
		return params.photoIds.map { delete(userId, it) }
	}

	fun delete(userId: String, photoId: String): ApiPhoto {
		val photoOwner = photoOwnerRepository.findByIdOrNull(PhotoOwnerId(userId, photoId))
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such photo owner: $userId, $photoId")

		val photo = photoRepository.findByIdOrNull(photoId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such photo: $photoId")

		log.debug { "[Delete Photo] Delete photo owner: $photoOwner" }
		photoOwnerRepository.delete(photoOwner)

		val albumPhotos = albumPhotoRepository.findByUserIdAndPhotoId(userId, photoId)
		log.debug { "[Delete Photo] Delete album photos: $albumPhotos" }
		albumPhotoRepository.deleteAll(albumPhotos)

		val numberOfOwner = photoOwnerRepository.countByPhotoId(photoId)
		if (numberOfOwner == 0) {
			log.debug { "[Delete Photo] Delete photo: $photo" }
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

		photoRepository.deleteById(photoId)
	}
}
