package kim.hyunsub.photo.bo.album

import kim.hyunsub.common.model.ApiPageResult
import kim.hyunsub.photo.config.PhotoConstants
import kim.hyunsub.photo.mapper.AlbumMapper
import kim.hyunsub.photo.model.api.ApiAlbumPreview
import kim.hyunsub.photo.repository.AlbumOwnerRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class AlbumSearchBo(
	private val albumOwnerRepository: AlbumOwnerRepository,
	private val albumMapper: AlbumMapper,
) {
	fun search(userId: String, p: Int): ApiPageResult<ApiAlbumPreview> {
		val total = albumOwnerRepository.countByUserId(userId)

		val page = PageRequest.of(p, PhotoConstants.PAGE_SIZE)
		val data = albumMapper.selectList(userId, page)
			.map { it.toPreview() }

		return ApiPageResult(
			total = total,
			page = p,
			pageSize = PhotoConstants.PAGE_SIZE,
			data = data,
		)
	}
}
