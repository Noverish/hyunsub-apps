package kim.hyunsub.apparel.controller

import kim.hyunsub.apparel.model.RestApiApparelImage
import kim.hyunsub.apparel.repository.ApparelImageRepository
import kim.hyunsub.apparel.repository.ApparelRepository
import kim.hyunsub.apparel.repository.entity.ApparelImage
import kim.hyunsub.apparel.service.FilePathConverter
import kim.hyunsub.common.api.ApiCaller
import kim.hyunsub.common.api.FileUrlConverter
import kim.hyunsub.common.log.Log
import kim.hyunsub.common.random.RandomGenerator
import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.SimpleResponse
import kim.hyunsub.common.web.model.UserAuth
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import kotlin.io.path.Path
import kotlin.io.path.extension

@Authorized(authorities = ["service_apparel"])
@RestController
@RequestMapping("/api/v1/apparels/{apparelId}/images")
class ApparelImageController(
	private val apiCaller: ApiCaller,
	private val apparelRepository: ApparelRepository,
	private val apparelImageRepository: ApparelImageRepository,
	private val fileUrlConverter: FileUrlConverter,
	private val randomGenerator: RandomGenerator,
) {
	companion object : Log

	@PostMapping("")
	fun upload(
		userAuth: UserAuth,
		@PathVariable apparelId: String,
		@RequestParam image: MultipartFile,
	): RestApiApparelImage {
		val userId = userAuth.idNo
		val fileName = image.originalFilename
			?: throw ErrorCodeException(ErrorCode.INVALID_PARAMETER)
		log.debug("[Apparel Image Upload] userId={}, apparelId={}, image={}", userId, apparelId, fileName)

		val apparel = apparelRepository.findByIdAndUserId(apparelId, userId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val fileExt = Path(fileName).extension
		val imageId = ApparelImage.generateId(randomGenerator)
		log.debug("[Apparel Image Upload] imageId={}", imageId)
		val imageName = "$imageId.$fileExt"

		val path = FilePathConverter.getApparelPhotoPath(userId, apparelId, imageName)
		apiCaller.upload(path, image.bytes)

		val apparelImage = ApparelImage(
			id = imageId,
			apparelId = apparelId,
			ext = fileExt
		)
		apparelImageRepository.saveAndFlush(apparelImage)

		if (apparel.imageId == null) {
			val newApparel = apparel.copy(imageId = imageId)
			apparelRepository.saveAndFlush(newApparel)
		}

		return RestApiApparelImage(
			imageId = imageId,
			apparelId = apparelId,
			url = fileUrlConverter.pathToUrl(path)
		)
	}

	@DeleteMapping("/{imageId}")
	fun delete(
		userAuth: UserAuth,
		@PathVariable apparelId: String,
		@PathVariable imageId: String,
	): SimpleResponse {
		val userId = userAuth.idNo
		log.debug("[Apparel Image Delete] userId={}, apparelId={}, imageId={}", userId, apparelId, imageId)

		apparelRepository.findByIdAndUserId(apparelId, userId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val apparelImage = apparelImageRepository.findByIdAndApparelId(imageId, apparelId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		apparelImageRepository.deleteById(imageId)

		val path = FilePathConverter.getApparelPhotoPath(userId, apparelId, apparelImage.fileName)
		apiCaller.unlink(path)

		return SimpleResponse()
	}
}
