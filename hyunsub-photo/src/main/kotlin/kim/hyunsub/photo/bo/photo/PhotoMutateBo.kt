package kim.hyunsub.photo.bo.photo

import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.photo.model.api.ApiPhoto
import kim.hyunsub.photo.model.api.toApi
import kim.hyunsub.photo.model.dto.PhotoDateUpdateParams
import kim.hyunsub.photo.repository.PhotoOwnerRepository
import kim.hyunsub.photo.repository.PhotoRepository
import kim.hyunsub.photo.repository.entity.PhotoOwnerId
import kim.hyunsub.photo.repository.generateId
import kim.hyunsub.photo.service.PhotoUpdateService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class PhotoMutateBo(
	private val photoOwnerRepository: PhotoOwnerRepository,
	private val photoRepository: PhotoRepository,
	private val photoUpdateService: PhotoUpdateService,
) {
	fun updatePhotoDate(userId: String, photoId: String, params: PhotoDateUpdateParams): ApiPhoto {
		val photoOwner = photoOwnerRepository.findByIdOrNull(PhotoOwnerId(userId, photoId))
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such photo owner")

		val photo = photoRepository.findByIdOrNull(photoId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such photo")

		val newId = photoRepository.generateId(params.date, photo.hash)

		val newPhoto = photoUpdateService.updateId(photo, newId)

		return newPhoto.toApi(photoOwner)
	}
}
