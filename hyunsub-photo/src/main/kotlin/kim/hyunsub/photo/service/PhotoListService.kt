package kim.hyunsub.photo.service

import kim.hyunsub.common.model.ApiPagination
import kim.hyunsub.photo.model.api.ApiPhotoPreview
import kim.hyunsub.photo.repository.mapper.PhotoMapper
import kim.hyunsub.photo.repository.mapper.PhotoOwnerMapper
import org.springframework.stereotype.Service

@Service
class PhotoListService(
	private val photoOwnerMapper: PhotoOwnerMapper,
	private val photoMapper: PhotoMapper,
) {
	fun listPhotoWithPhotoId(userId: String, photoId: String): ApiPagination<ApiPhotoPreview> {
		TODO()
		// val total = photoOwnerMapper.count(PhotoOwnerCondition(userId = userId))
		// val pageRequest = PageRequest.of(0, PhotoConstants.PAGE_SIZE)
		//
		// val before = photoMapper.select(
		// 	PhotoCondition(
		// 		userId = userId,
		// 		idGreaterThan = photoId,
		// 		page = pageRequest,
		// 		asc = true,
		// 	)
		// )
		//
		// val photo = photoMapper.selectOne2(id = photoId, userId = userId)
		//
		// val after = photoMapper.select(
		// 	PhotoCondition(
		// 		userId = userId,
		// 		idLessThan = photoId,
		// 		page = pageRequest,
		// 		asc = false,
		// 	)
		// )
		//
		// val prev = if (before.size == pageRequest.pageSize) before.lastOrNull()?.id else null
		// val next = if (after.size == pageRequest.pageSize) after.lastOrNull()?.id else null
		//
		// val data = buildList {
		// 	addAll(before)
		// 	add(photo)
		// 	addAll(after)
		// }
		// 	.filterNotNull()
		// 	.map { it.toApi() }
		//
		// return ApiPagination(
		// 	total = total,
		// 	prev = prev,
		// 	next = next,
		// 	data = data
		// )
	}

	fun list(userId: String, next: String? = null, prev: String? = null): ApiPagination<ApiPhotoPreview> {
		TODO()
		// val total = photoOwnerMapper.count(PhotoOwnerCondition(userId = userId))
		// val pageRequest = PageRequest.of(0, PhotoConstants.PAGE_SIZE)
		//
		// val data = when {
		// 	prev != null -> photoMapper.select(
		// 		PhotoCondition(
		// 			userId = userId,
		// 			idGreaterThan = prev,
		// 			page = pageRequest,
		// 			asc = true,
		// 		)
		// 	)
		// 	next != null -> photoMapper.select(
		// 		PhotoCondition(
		// 			userId = userId,
		// 			idLessThan = next,
		// 			page = pageRequest,
		// 			asc = false,
		// 		)
		// 	)
		// 	else -> photoMapper.select(
		// 		PhotoCondition(
		// 			userId = userId,
		// 			page = pageRequest,
		// 		)
		// 	)
		// }
		// 	.map { it.toApi() }
		//
		// val full = data.size == pageRequest.pageSize
		//
		// val prevResult = when {
		// 	prev != null -> if (full) data.firstOrNull()?.id else null
		// 	next != null -> data.firstOrNull()?.id
		// 	else -> null
		// }
		//
		// val nextResult = when {
		// 	prev != null -> data.lastOrNull()?.id
		// 	next != null -> if (full) data.lastOrNull()?.id else null
		// 	else -> if (full) data.lastOrNull()?.id else null
		// }
		//
		// return ApiPagination(
		// 	total = total,
		// 	prev = prevResult,
		// 	next = nextResult,
		// 	data = data,
		// )
	}
}
