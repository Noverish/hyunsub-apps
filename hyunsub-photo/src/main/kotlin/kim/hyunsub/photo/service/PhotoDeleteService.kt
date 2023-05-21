package kim.hyunsub.photo.service

import kim.hyunsub.common.api.ApiCaller
import kim.hyunsub.common.fs.FsClient
import kim.hyunsub.common.fs.remove
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
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
class PhotoDeleteService(
	private val apiCaller: ApiCaller,
	private val fsClient: FsClient,
	private val photoRepository: PhotoRepository,
	private val photoOwnerRepository: PhotoOwnerRepository,
	private val albumPhotoRepository: AlbumPhotoRepository,
) {
	private val log = KotlinLogging.logger { }

	fun delete(userId: String, photoId: String): Photo {
		val photoOwner = photoOwnerRepository.findByIdOrNull(PhotoOwnerId(userId, photoId))
			?: run {
				log.debug { "[Delete Photo] No such photo owner: $userId, $photoId" }
				throw ErrorCodeException(ErrorCode.NOT_FOUND)
			}

		val photo = photoRepository.findByIdOrNull(photoId)
			?: run {
				log.error { "[Delete Photo] No such photo: $photoId" }
				throw ErrorCodeException(ErrorCode.INTERNAL_SERVER_ERROR)
			}

		log.debug { "[Delete Photo] Delete photo owner: $photoOwner" }
		photoOwnerRepository.delete(photoOwner)

		val albumPhotos = albumPhotoRepository.findByUserIdAndPhotoId(userId, photoId)
		log.debug { "[Delete Photo] Delete album photos: $albumPhotos" }
		albumPhotoRepository.deleteAll(albumPhotos)

		val numberOfOwner = photoOwnerRepository.countByPhotoId(photoId)
		if (numberOfOwner > 0) {
			return photo
		}

		log.debug { "[Delete Photo] Delete photo: $photo" }
		deleteFile(photo)
		return photo
	}

	private fun deleteFile(photo: Photo) {
		val photoId = photo.id
		val originalPath = PhotoPathConverter.original(photo)
		val thumbnailPath = PhotoPathConverter.thumbnail(photoId)

		fsClient.remove(originalPath)
		fsClient.remove(thumbnailPath)

		if (photo.isVideo) {
			val videoPath = PhotoPathConverter.video(photoId)
			try {
				fsClient.remove(videoPath)
			} catch (_: Exception) {
			}
		}

		photoRepository.deleteById(photoId)
	}
}
