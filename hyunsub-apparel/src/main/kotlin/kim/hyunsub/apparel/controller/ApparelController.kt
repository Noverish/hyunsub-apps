package kim.hyunsub.apparel.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import kim.hyunsub.apparel.model.RestApiApparel
import kim.hyunsub.apparel.model.RestApiApparelPreview
import kim.hyunsub.apparel.repository.ApparelImageRepository
import kim.hyunsub.apparel.repository.ApparelPreviewRepository
import kim.hyunsub.apparel.repository.ApparelRepository
import kim.hyunsub.apparel.repository.entity.Apparel
import kim.hyunsub.apparel.service.ApiModelConverter
import kim.hyunsub.common.log.Log
import kim.hyunsub.common.model.RestApiPageResult
import kim.hyunsub.common.random.RandomGenerator
import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.UserAuth
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.*

@Authorized(authorities = ["service_apparel"])
@RestController
@RequestMapping("/api/v1/apparels")
class ApparelController(
	private val apparelRepository: ApparelRepository,
	private val apparelPreviewRepository: ApparelPreviewRepository,
	private val apparelImageRepository: ApparelImageRepository,
	private val apiModelConverter: ApiModelConverter,
	private val mapper: ObjectMapper,
	private val randomGenerator: RandomGenerator,
) {
	companion object : Log

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

	@PostMapping("")
	fun create(
		userAuth: UserAuth,
		@RequestBody body: Map<String, Any?>,
	): RestApiApparel {
		val userId = userAuth.idNo
		val id = Apparel.generateId(randomGenerator)

		val map = buildMap {
			this += body.mapValues { if (it.value == "") null else it.value }
			this["id"] = id
			this["userId"] = userId
		}

		log.debug("[Create Apparel] map={}", map)
		val apparel = mapper.convertValue<Apparel>(map)
		apparelRepository.saveAndFlush(apparel)
		log.debug("[Create Apparel] apparel={}", apparel)

		return apiModelConverter.convert(userId, apparel, emptyList())
	}
}
