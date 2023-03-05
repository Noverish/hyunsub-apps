package kim.hyunsub.photo.controller

import kim.hyunsub.common.web.config.userAuth
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.photo.model.dto.PhotoUploadParams
import kim.hyunsub.photo.model.dto.PhotoUploadResult
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
class PhotoUploadControllerV2(
	private val photoUploadService: PhotoUploadService,
) {
	private val log = KotlinLogging.logger { }

	@PostMapping("")
	fun upload(
		userAuth: UserAuth,
		@RequestBody params: PhotoUploadParams,
	): PhotoUploadResult {
		val userId = userAuth.idNo
		log.debug { "[PhotoUpload] userId=$userId, params=$params" }
		photoUploadService.upload(userId, params)
		return PhotoUploadResult.success(params.path)
	}

	@MessageMapping("/v1/photo/upload/{nonce}/request")
	@SendTo("/v1/photo/upload/{nonce}/response")
	fun upload2(
		@DestinationVariable nonce: String,
		params: PhotoUploadParams,
		accessor: SimpMessageHeaderAccessor,
	): PhotoUploadResult {
		return try {
			val userId = accessor.userAuth.idNo
			log.debug { "[PhotoUpload] nonce=$nonce, userId=$userId, params=$params" }
			photoUploadService.upload(userId, params)
			PhotoUploadResult.success(params.path)
		} catch (ex: Exception) {
			PhotoUploadResult.failure(params.path, ex)
		}
	}
}
