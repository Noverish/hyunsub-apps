package kim.hyunsub.apparel.controller

import kim.hyunsub.apparel.config.ApparelConstants
import kim.hyunsub.apparel.model.RestApiApparelDetail
import kim.hyunsub.apparel.model.RestApiApparelPreview
import kim.hyunsub.apparel.model.dto.ApparelUpsertParams
import kim.hyunsub.apparel.model.toDto
import kim.hyunsub.apparel.repository.ApparelImageRepository
import kim.hyunsub.apparel.repository.ApparelPreviewRepository
import kim.hyunsub.apparel.repository.ApparelRepository
import kim.hyunsub.apparel.service.ApparelService
import kim.hyunsub.common.model.ApiPageResult
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.UserAuth
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/apparels")
class ApparelController(
	private val apparelService: ApparelService,
	private val apparelRepository: ApparelRepository,
	private val apparelPreviewRepository: ApparelPreviewRepository,
	private val apparelImageRepository: ApparelImageRepository,
) {
	@GetMapping("")
	fun list(
		userAuth: UserAuth,
		@RequestParam(defaultValue = "0") p: Int,
	): ApiPageResult<RestApiApparelPreview> {
		val userId = userAuth.idNo

		val total = apparelPreviewRepository.countByUserId(userId)
		val pageRequest = PageRequest.of(p, ApparelConstants.PAGE_SIZE)
		val list = apparelPreviewRepository.findByUserId(userId, pageRequest)
			.map { it.toDto(userId) }

		return ApiPageResult(total, p, ApparelConstants.PAGE_SIZE, list)
	}

	@GetMapping("/{apparelId}")
	fun detail(
		userAuth: UserAuth,
		@PathVariable apparelId: String,
	): RestApiApparelDetail {
		val userId = userAuth.idNo

		val apparel = apparelRepository.findByIdAndUserId(apparelId, userId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val images = apparelImageRepository.findByApparelIdOrderByRegDt(apparelId)

		return RestApiApparelDetail(
			apparel = apparel.toDto(),
			images = images.map { it.toDto(userId) }
		)
	}

	@PostMapping("")
	fun create(
		userAuth: UserAuth,
		@RequestBody params: ApparelUpsertParams,
	): RestApiApparelDetail {
		return apparelService.create(userAuth.idNo, params)
	}

	@PutMapping("/{apparelId}")
	fun update(
		userAuth: UserAuth,
		@PathVariable apparelId: String,
		@RequestBody params: ApparelUpsertParams,
	): RestApiApparelDetail {
		return apparelService.update(userAuth.idNo, apparelId, params)
	}

	@DeleteMapping("/{apparelId}")
	fun delete(
		userAuth: UserAuth,
		@PathVariable apparelId: String,
	): RestApiApparelDetail {
		return apparelService.delete(userAuth.idNo, apparelId)
	}
}
