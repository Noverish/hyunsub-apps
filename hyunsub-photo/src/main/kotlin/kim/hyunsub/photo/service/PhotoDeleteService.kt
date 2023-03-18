package kim.hyunsub.photo.service

import kim.hyunsub.common.api.ApiCaller
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.photo.repository.PhotoOwnerRepository
import kim.hyunsub.photo.repository.PhotoV2Repository
import kim.hyunsub.photo.repository.entity.PhotoOwnerId
import kim.hyunsub.photo.repository.entity.PhotoV2
import kim.hyunsub.photo.util.PhotoPathUtils
import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class PhotoDeleteService(
	private val apiCaller: ApiCaller,
	private val photoRepository: PhotoV2Repository,
	private val photoOwnerRepository: PhotoOwnerRepository,
) {
	private val log = KotlinLogging.logger { }

	fun delete(userId: String, photoId: String): PhotoV2 {
		val photoOwnerId = PhotoOwnerId(userId, photoId)

		photoOwnerRepository.findByIdOrNull(photoOwnerId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val photo = photoRepository.findByIdOrNull(photoId)
			?: throw ErrorCodeException(ErrorCode.INTERNAL_SERVER_ERROR)

		photoOwnerRepository.deleteById(photoOwnerId)
		val numberOfOwner = photoOwnerRepository.countByPhotoId(photoId)
		log.debug { "[Delete Photo] # of owner: $numberOfOwner" }
		if (numberOfOwner > 0) {
			return photo
		}

		val originalPath = PhotoPathUtils.original(photo)
		val thumbnailPath = PhotoPathUtils.thumbnail(photo)

		apiCaller.remove(originalPath)
		apiCaller.remove(thumbnailPath)

		photoRepository.deleteById(photoId)

		return photo
	}
}
