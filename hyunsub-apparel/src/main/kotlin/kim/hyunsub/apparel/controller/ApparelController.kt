package kim.hyunsub.apparel.controller

import kim.hyunsub.apparel.model.RestApiApparelDetail
import kim.hyunsub.apparel.model.RestApiApparelPreview
import kim.hyunsub.apparel.model.dto.ApparelUpsertParams
import kim.hyunsub.apparel.model.toDto
import kim.hyunsub.apparel.repository.ApparelImageRepository
import kim.hyunsub.apparel.repository.ApparelPreviewRepository
import kim.hyunsub.apparel.repository.ApparelRepository
import kim.hyunsub.apparel.service.ApiModelConverter
import kim.hyunsub.apparel.service.ApparelService
import kim.hyunsub.common.model.RestApiPageResult
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
	private val apiModelConverter: ApiModelConverter,
) {
	@GetMapping("")
	fun list(
		userAuth: UserAuth,
		@RequestParam(defaultValue = "0") p: Int,
	): RestApiPageResult<RestApiApparelPreview> {
		val userId = userAuth.idNo

		// TODO 48을 어디 따로 저장하기
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
	): RestApiApparelDetail {
		val userId = userAuth.idNo

		val apparel = apparelRepository.findByIdAndUserId(apparelId, userId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val images = apparelImageRepository.findByApparelId(apparelId)

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
