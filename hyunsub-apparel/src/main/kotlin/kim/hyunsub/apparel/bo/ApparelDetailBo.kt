package kim.hyunsub.apparel.bo

import kim.hyunsub.apparel.model.ApiApparel
import kim.hyunsub.apparel.model.toApi
import kim.hyunsub.apparel.model.toApiInfo
import kim.hyunsub.apparel.repository.ApparelImageRepository
import kim.hyunsub.apparel.repository.ApparelRepository
import org.springframework.stereotype.Service

@Service
class ApparelDetailBo(
	private val apparelRepository: ApparelRepository,
	private val apparelImageRepository: ApparelImageRepository,
) {
	fun detail(userId: String, apparelId: String): ApiApparel? {
		val apparel = apparelRepository.findByIdAndUserId(apparelId, userId)
			?: return null

		val images = apparelImageRepository.findByApparelIdOrderByRegDt(apparelId)

		return ApiApparel(
			id = apparelId,
			info = apparel.toApiInfo(),
			images = images.map { it.toApi(userId) }
		)
	}
}
