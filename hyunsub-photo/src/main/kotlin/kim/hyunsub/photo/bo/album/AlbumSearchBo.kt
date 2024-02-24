package kim.hyunsub.photo.bo.album

import kim.hyunsub.common.model.ApiPageResult
import kim.hyunsub.photo.config.PhotoConstants
import kim.hyunsub.photo.model.api.ApiAlbumPreview
import kim.hyunsub.photo.repository.condition.AlbumCondition
import kim.hyunsub.photo.repository.condition.AlbumOwnerCondition
import kim.hyunsub.photo.repository.mapper.AlbumMapper
import kim.hyunsub.photo.repository.mapper.AlbumOwnerMapper
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class AlbumSearchBo(
	private val albumOwnerMapper: AlbumOwnerMapper,
	private val albumMapper: AlbumMapper,
) {
	fun search(userId: String, p: Int): ApiPageResult<ApiAlbumPreview> {
		val total = albumOwnerMapper.count(AlbumOwnerCondition(userId = userId))

		val page = PageRequest.of(p, PhotoConstants.PAGE_SIZE)
		val data = albumMapper.select(AlbumCondition(userId = userId, page = page))
			.map { it.toPreview() }

		return ApiPageResult(
			total = total,
			page = p,
			pageSize = PhotoConstants.PAGE_SIZE,
			data = data,
		)
	}
}
