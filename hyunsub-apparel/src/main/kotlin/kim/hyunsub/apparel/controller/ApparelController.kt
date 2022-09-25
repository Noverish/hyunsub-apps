package kim.hyunsub.apparel.controller

import kim.hyunsub.apparel.model.RestApiApparel
import kim.hyunsub.apparel.model.RestApiApparelPreview
import kim.hyunsub.apparel.repository.ApparelImageRepository
import kim.hyunsub.apparel.repository.ApparelPreviewRepository
import kim.hyunsub.apparel.repository.ApparelRepository
import kim.hyunsub.apparel.service.ApiModelConverter
import kim.hyunsub.common.model.RestApiPageResult
import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.UserAuth
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Authorized(authorities = ["service_apparel"])
@RestController
@RequestMapping("/api/v1/apparels")
class ApparelController(
	private val apparelRepository: ApparelRepository,
	private val apparelPreviewRepository: ApparelPreviewRepository,
	private val apparelImageRepository: ApparelImageRepository,
	private val apiModelConverter: ApiModelConverter,
) {
	@GetMapping("")
	fun list(
		userAuth: UserAuth,
		@RequestParam p: Int,
	): RestApiPageResult<RestApiApparelPreview> {
		val userId = userAuth.idNo

		val total = apparelPreviewRepository.countByUserId(userId)
		val pageRequest = PageRequest.of(p, 48)
		val list = apparelPreviewRepository.findByUserId(userId, pageRequest)
			.map { apiModelConverter.convert(userId, it) }

		return RestApiPageResult(total, p, 48, list)
	}

	@GetMapping("/{apparelId}")
	fun detail(
		userAuth: UserAuth,
		@PathVariable apparelId: String,
	): RestApiApparel {
		val userId = userAuth.idNo

		val apparel = apparelRepository.findByIdAndUserId(apparelId, userId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val photos = apparelImageRepository.findByApparelId(apparelId)

		return apiModelConverter.convert(userId, apparel, photos)
	}
}
