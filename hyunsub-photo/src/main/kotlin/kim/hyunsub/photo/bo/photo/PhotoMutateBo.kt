package kim.hyunsub.photo.bo.photo

import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.photo.model.api.ApiPhoto
import kim.hyunsub.photo.model.api.toApi
import kim.hyunsub.photo.model.dto.PhotoDateUpdateParams
import kim.hyunsub.photo.repository.mapper.PhotoMapper
import kim.hyunsub.photo.repository.mapper.PhotoOwnerMapper
import kim.hyunsub.photo.repository.mapper.generateId
import kim.hyunsub.photo.service.PhotoUpdateService
import org.springframework.stereotype.Service

@Service
class PhotoMutateBo(
	private val photoOwnerMapper: PhotoOwnerMapper,
	private val photoMapper: PhotoMapper,
	private val photoUpdateService: PhotoUpdateService,
) {
	fun updatePhotoDate(userId: String, photoId: String, params: PhotoDateUpdateParams): ApiPhoto {
		val photoOwner = photoOwnerMapper.selectOne(userId = userId, photoId = photoId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such photo owner")

		val photo = photoMapper.selectOne(photoId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such photo")

		val newId = photoMapper.generateId(params.date, photo.hash)

		val newPhoto = photoUpdateService.updateId(photo, newId)

		return newPhoto.toApi(photoOwner)
	}
}
