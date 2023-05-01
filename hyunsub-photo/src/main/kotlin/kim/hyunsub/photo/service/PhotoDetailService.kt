package kim.hyunsub.photo.service

import kim.hyunsub.common.util.getHumanReadableSize
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.photo.model.api.ApiPhoto
import kim.hyunsub.photo.repository.PhotoOwnerRepository
import kim.hyunsub.photo.repository.PhotoRepository
import kim.hyunsub.photo.repository.entity.PhotoOwnerId
import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class PhotoDetailService(
	private val photoRepository: PhotoRepository,
	private val photoOwnerRepository: PhotoOwnerRepository,
) {
	private val log = KotlinLogging.logger { }

	fun detail(userId: String, photoId: String): ApiPhoto {
		val photoOwner = photoOwnerRepository.findByIdOrNull(PhotoOwnerId(userId, photoId))
			?: run {
				log.debug { "[Detail Photo] No such photo owner: $userId, $photoId" }
				throw ErrorCodeException(ErrorCode.NOT_FOUND)
			}

		val photo = photoRepository.findByIdOrNull(photoId)
			?: run {
				log.error { "[Detail Photo] No such photo: $photoId" }
				throw ErrorCodeException(ErrorCode.INTERNAL_SERVER_ERROR)
			}

		return ApiPhoto(
			id = photo.id,
			imageSize = "${photo.width} x ${photo.height}",
			fileSize = getHumanReadableSize(photo.size.toLong()),
			date = photo.date,
			regDt = photoOwner.regDt,
			fileName = photoOwner.name,
			dateType = photo.dateType,
		)
	}
}
