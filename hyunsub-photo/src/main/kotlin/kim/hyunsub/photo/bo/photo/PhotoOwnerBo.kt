package kim.hyunsub.photo.bo.photo

import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.SimpleResponse
import kim.hyunsub.photo.model.api.ApiUpdatePhotoOffsetParams
import kim.hyunsub.photo.repository.mapper.PhotoOwnerMapper
import org.springframework.stereotype.Service

@Service
class PhotoOwnerBo(
	private val photoOwnerMapper: PhotoOwnerMapper,
) {
	fun updateOffsetSameLocal(userId: String, params: ApiUpdatePhotoOffsetParams): SimpleResponse {
		val photoOwner = photoOwnerMapper.selectOne(userId, params.photoId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val newPhotoOwner = photoOwner.copy(offset = params.hour * 3600)
		photoOwnerMapper.update(newPhotoOwner)

		return SimpleResponse()
	}
}
