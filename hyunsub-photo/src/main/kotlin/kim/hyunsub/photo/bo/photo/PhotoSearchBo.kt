package kim.hyunsub.photo.bo.photo

import kim.hyunsub.common.model.ApiPageResult
import kim.hyunsub.common.util.toMillis
import kim.hyunsub.photo.config.PhotoConstants
import kim.hyunsub.photo.mapper.PhotoMapper
import kim.hyunsub.photo.model.api.ApiPhotoPreview
import kim.hyunsub.photo.model.api.toApiPreview
import kim.hyunsub.photo.model.dto.PhotoSearchParams
import kim.hyunsub.photo.repository.entity.Photo
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class PhotoSearchBo(
	private val photoMapper: PhotoMapper,
) {
	fun search(userId: String, params: PhotoSearchParams): ApiPageResult<ApiPhotoPreview> {
		val page = PageRequest.of(params.page ?: 0, params.pageSize ?: PhotoConstants.PAGE_SIZE)
		return searchByDate(userId, params.date, page)
	}

	fun searchByDate(userId: String, date: LocalDate, page: Pageable): ApiPageResult<ApiPhotoPreview> {
		val startMillis = date.atStartOfDay().toMillis()
		val endMillis = date.plusDays(1).atStartOfDay().toMillis()

		val start = Photo.generateId(startMillis, "00000")
		val end = Photo.generateId(endMillis, "00000")

		val total = photoMapper.count(userId, start, end)
		val result = photoMapper.select(userId, start, end, page)

		return ApiPageResult(
			total = total,
			page = page.pageNumber,
			pageSize = page.pageSize,
			data = result.map { it.toApiPreview() }
		)
	}
}
