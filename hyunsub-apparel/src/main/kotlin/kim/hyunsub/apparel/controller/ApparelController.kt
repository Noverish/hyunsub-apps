package kim.hyunsub.apparel.controller

import kim.hyunsub.apparel.model.RestApiApparel
import kim.hyunsub.apparel.repository.ApparelPhotoRepository
import kim.hyunsub.apparel.repository.ApparelRepository
import kim.hyunsub.apparel.repository.entity.Apparel
import kim.hyunsub.apparel.service.ApiModelConverter
import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.UserAuth
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Authorized(authorities = ["service_apparel"])
@RestController
@RequestMapping("/api/v1/apparel")
class ApparelController(
	private val apparelRepository: ApparelRepository,
	private val apparelPhotoRepository: ApparelPhotoRepository,
	private val apiModelConverter: ApiModelConverter,
) {
	@GetMapping("")
	fun list(
		userAuth: UserAuth,
	): List<RestApiApparel> {
		val userId = userAuth.idNo

		val apparels = apparelRepository.findByUserId(userId)

		return apparels.map {
			apiModelConverter.convert(userId, it)
		}
	}

	@GetMapping("/{apparelId}")
	fun detail(
		userAuth: UserAuth,
		@PathVariable apparelId: String,
	): RestApiApparel {
		val apparel = apparelRepository.findByIdOrNull(apparelId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val userId = userAuth.idNo
		if (apparel.userId != userId) {
			throw ErrorCodeException(ErrorCode.NOT_FOUND)
		}

		val photos = apparelPhotoRepository.findByApparelId(apparelId)

		return apiModelConverter.convert(userId, apparel, photos)
	}
}
