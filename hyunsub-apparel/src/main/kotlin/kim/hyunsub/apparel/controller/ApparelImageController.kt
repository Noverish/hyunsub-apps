package kim.hyunsub.apparel.controller

import kim.hyunsub.apparel.model.RestApiApparelImage
import kim.hyunsub.apparel.service.ApiModelConverter
import kim.hyunsub.apparel.service.ApparelImageService
import kim.hyunsub.common.log.Log
import kim.hyunsub.common.web.model.SimpleResponse
import kim.hyunsub.common.web.model.UserAuth
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/apparels/{apparelId}/images")
class ApparelImageController(
	private val apparelImageService: ApparelImageService,
	private val apiModelConverter: ApiModelConverter,
) {
	companion object : Log

	@PostMapping("")
	fun upload(
		userAuth: UserAuth,
		@PathVariable apparelId: String,
		@RequestParam image: MultipartFile,
	): RestApiApparelImage {
		val userId = userAuth.idNo
		val apparelImage = apparelImageService.upload(userId, apparelId, image)
		return apiModelConverter.convert(userId, apparelImage)
	}

	@DeleteMapping("/{imageId}")
	fun delete(
		userAuth: UserAuth,
		@PathVariable apparelId: String,
		@PathVariable imageId: String,
	): SimpleResponse {
		apparelImageService.delete(userAuth.idNo, apparelId, imageId)
		return SimpleResponse()
	}
}
