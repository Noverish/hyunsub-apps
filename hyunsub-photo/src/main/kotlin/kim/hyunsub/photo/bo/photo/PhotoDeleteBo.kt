package kim.hyunsub.photo.bo.photo

import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.SimpleResponse
import kim.hyunsub.photo.model.dto.PhotoDeleteParams
import kim.hyunsub.photo.repository.condition.AlbumPhotoCondition
import kim.hyunsub.photo.repository.condition.PhotoOwnerCondition
import kim.hyunsub.photo.repository.mapper.AlbumPhotoMapper
import kim.hyunsub.photo.repository.mapper.PhotoOwnerMapper
import kim.hyunsub.photo.service.AlbumThumbnailService
import kim.hyunsub.photo.service.PhotoDeleteService
import org.springframework.stereotype.Service

@Service
class PhotoDeleteBo(
	private val photoOwnerMapper: PhotoOwnerMapper,
	private val albumPhotoMapper: AlbumPhotoMapper,
	private val photoDeleteService: PhotoDeleteService,
	private val albumThumbnailService: AlbumThumbnailService,
) {
	fun delete(userId: String, params: PhotoDeleteParams): SimpleResponse {
		val photoIds = params.photoIds

		val photoOwnerCondition = PhotoOwnerCondition(userId = userId, photoIds = photoIds)
		val photoOwnerCnt = photoOwnerMapper.count(photoOwnerCondition)
		if (photoOwnerCnt != photoIds.size) {
			throw ErrorCodeException(ErrorCode.INVALID_PARAMETER, "Unauthorized photos")
		}

		photoOwnerMapper.deletePhotosOfOneUser(userId, photoIds)
		albumPhotoMapper.delete(AlbumPhotoCondition(userId = userId, photoIds = photoIds))

		albumThumbnailService.unregisterAsync(photoIds)
		photoDeleteService.checkAndDeleteAsync(photoIds)

		return SimpleResponse()
	}
}
