package kim.hyunsub.apparel.bo

import kim.hyunsub.apparel.model.ApiApparel
import kim.hyunsub.apparel.model.dto.ApparelCreateParams
import kim.hyunsub.apparel.model.dto.ApparelUpdateParams
import kim.hyunsub.apparel.model.toApi
import kim.hyunsub.apparel.model.toApiInfo
import kim.hyunsub.apparel.repository.ApparelImageRepository
import kim.hyunsub.apparel.repository.ApparelRepository
import kim.hyunsub.apparel.repository.entity.Apparel
import kim.hyunsub.apparel.repository.generateId
import kim.hyunsub.apparel.service.ApparelImageService
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ApparelMutateBo(
	private val apparelRepository: ApparelRepository,
	private val apparelImageService: ApparelImageService,
	private val apparelImageRepository: ApparelImageRepository,
) {
	fun create(userId: String, params: ApparelCreateParams): ApiApparel {
		val apparelId = apparelRepository.generateId()

		val images = apparelImageService.create(userId, apparelId, params.uploads)

		val info = params.info
		val apparel = Apparel(
			id = apparelId,
			userId = userId,
			itemNo = info.itemNo,
			name = info.name,
			brand = info.brand,
			category = info.category,
			size = info.size,
			color = info.color,
			originPrice = info.originPrice,
			discountPrice = info.discountPrice,
			material = info.material,
			size2 = info.size2,
			buyDt = info.buyDt,
			buyLoc = info.buyLoc,
			makeDt = info.makeDt,
			imageId = images.firstOrNull()?.id,
			regDt = LocalDateTime.now(),
			discardDt = null,
		)

		apparelRepository.save(apparel)

		return ApiApparel(
			id = apparelId,
			info = apparel.toApiInfo(),
			images = images.map { it.toApi(userId) }
		)
	}

	fun update(userId: String, apparelId: String, params: ApparelUpdateParams): ApiApparel {
		val oldApparel = apparelRepository.findByIdAndUserId(apparelId, userId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		apparelImageService.deleteWithIds(userId, params.deletes)
		apparelImageService.create(userId, apparelId, params.uploads)
		val images = apparelImageRepository.findByApparelIdOrderByRegDt(apparelId)

		val info = params.info
		val newApparel = oldApparel.copy(
			itemNo = info.itemNo,
			name = info.name,
			brand = info.brand,
			category = info.category,
			size = info.size,
			color = info.color,
			originPrice = info.originPrice,
			discountPrice = info.discountPrice,
			material = info.material,
			size2 = info.size2,
			buyDt = info.buyDt,
			buyLoc = info.buyLoc,
			makeDt = info.makeDt,
			imageId = images.firstOrNull()?.id,
			discardDt = when {
				oldApparel.discardDt == null && info.discarded -> LocalDateTime.now()
				oldApparel.discardDt != null && !info.discarded -> null
				else -> oldApparel.discardDt
			},
		).copy()

		apparelRepository.save(newApparel)

		return ApiApparel(
			id = apparelId,
			info = newApparel.toApiInfo(),
			images = images.map { it.toApi(userId) }
		)
	}

	fun delete(userId: String, apparelId: String): ApiApparel {
		val apparel = apparelRepository.findByIdAndUserId(apparelId, userId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)
		val images = apparelImageRepository.findByApparelIdOrderByRegDt(apparelId)

		apparelImageService.delete(userId, images)
		apparelRepository.delete(apparel)

		return ApiApparel(
			id = apparelId,
			info = apparel.toApiInfo(),
			images = images.map { it.toApi(userId) }
		)
	}
}
