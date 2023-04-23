package kim.hyunsub.photo.service

import kim.hyunsub.common.model.RestApiPagination
import kim.hyunsub.photo.config.PhotoConstants
import kim.hyunsub.photo.model.api.ApiPhotoPreview
import kim.hyunsub.photo.repository.PhotoOwnerRepository
import kim.hyunsub.photo.repository.PhotoRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class PhotoListService(
	private val photoRepository: PhotoRepository,
	private val photoOwnerRepository: PhotoOwnerRepository,
) {
	fun listPhotoWithPhotoId(userId: String, photoId: String): RestApiPagination<ApiPhotoPreview> {
		val total = photoOwnerRepository.countByUserId(userId)
		val pageRequest = PageRequest.of(0, PhotoConstants.PHOTO_PAGE_SIZE)

		val before = photoRepository.selectMyPhotosWithPrev(userId, photoId, pageRequest)
		val photo = photoRepository.selectMyPhotosWithPhotoId(userId, photoId)
		val after = photoRepository.selectMyPhotosWithNext(userId, photoId, pageRequest)

		val prev = if (before.size == pageRequest.pageSize) before.lastOrNull()?.id else null
		val next = if (after.size == pageRequest.pageSize) after.lastOrNull()?.id else null

		val data = buildList {
			addAll(before)
			add(photo)
			addAll(after)
		}
			.filterNotNull()
			.sortedByDescending { it.id }
			.map { it.toPreview() }

		return RestApiPagination(
			total = total,
			prev = prev,
			next = next,
			data = data
		)
	}

	fun list(userId: String, next: String? = null, prev: String? = null): RestApiPagination<ApiPhotoPreview> {
		val total = photoOwnerRepository.countByUserId(userId)
		val pageRequest = PageRequest.of(0, PhotoConstants.PHOTO_PAGE_SIZE)

		val data = when {
			prev != null -> photoRepository.selectMyPhotosWithPrev(userId, prev, pageRequest)
			next != null -> photoRepository.selectMyPhotosWithNext(userId, next, pageRequest)
			else -> photoRepository.selectMyPhotos(userId, pageRequest)
		}
			.sortedByDescending { it.id.lowercase() }
			.map { it.toPreview() }

		val full = data.size == pageRequest.pageSize

		val prevResult = when {
			prev != null -> if (full) data.firstOrNull()?.id else null
			next != null -> data.firstOrNull()?.id
			else -> null
		}

		val nextResult = when {
			prev != null -> data.lastOrNull()?.id
			next != null -> if (full) data.lastOrNull()?.id else null
			else -> if (full) data.lastOrNull()?.id else null
		}

		return RestApiPagination(
			total = total,
			prev = prevResult,
			next = nextResult,
			data = data,
		)
	}
}
