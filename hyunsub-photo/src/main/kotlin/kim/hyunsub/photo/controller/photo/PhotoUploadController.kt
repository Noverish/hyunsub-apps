package kim.hyunsub.photo.controller.photo

import kim.hyunsub.common.web.config.userAuth
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.photo.model.api.ApiPhotoUploadParams
import kim.hyunsub.photo.model.api.ApiPhotoUploadResult
import kim.hyunsub.photo.model.api.toApiPreview
import kim.hyunsub.photo.service.PhotoUploadService
import mu.KotlinLogging
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/photos/upload")
class PhotoUploadController(
	private val photoUploadService: PhotoUploadService,
) {
	private val log = KotlinLogging.logger { }

	@PostMapping("")
	fun uploadViaHttp(
		userAuth: UserAuth,
		@RequestBody params: ApiPhotoUploadParams,
	): ApiPhotoUploadResult {
		val userId = userAuth.idNo
		log.debug { "[PhotoUpload] userId=$userId, params=$params" }
		val preview = photoUploadService.upload(userId, params).toApiPreview()
		return ApiPhotoUploadResult.success(params.nonce, preview)
	}

	@MessageMapping("/v1/photo/upload/{nonce}/request")
	@SendTo("/v1/photo/upload/{nonce}/response")
	fun uploadViaSocket(
		@DestinationVariable nonce: String,
		params: ApiPhotoUploadParams,
		accessor: SimpMessageHeaderAccessor,
	): ApiPhotoUploadResult {
		return try {
			val userId = accessor.userAuth.idNo
			log.debug { "[PhotoUpload] nonce=$nonce, userId=$userId, params=$params" }
			val preview = photoUploadService.upload(userId, params).toApiPreview()
			ApiPhotoUploadResult.success(params.nonce, preview)
		} catch (ex: Exception) {
			ex.printStackTrace()
			ApiPhotoUploadResult.failure(params.nonce, ex)
		}
	}
}
