package kim.hyunsub.photo.controller

import com.fasterxml.jackson.databind.ObjectMapper
import kim.hyunsub.common.api.ApiCaller
import kim.hyunsub.common.api.FileUrlConverter
import kim.hyunsub.common.log.Log
import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.photo.config.PhotoConstants
import kim.hyunsub.photo.model.RestApiPhoto
import kim.hyunsub.photo.model.RestApiPhotoUploadParams
import kim.hyunsub.photo.repository.AlbumRepository
import kim.hyunsub.photo.repository.PhotoMetadataRepository
import kim.hyunsub.photo.repository.PhotoRepository
import kim.hyunsub.photo.repository.entity.Photo
import kim.hyunsub.photo.repository.entity.PhotoMetadata
import kim.hyunsub.photo.service.ApiModelConverter
import kim.hyunsub.photo.service.EncodeApiCaller
import kim.hyunsub.photo.service.PhotoMetadataDateParser
import kim.hyunsub.photo.service.ThumbnailService
import kim.hyunsub.photo.util.isImage
import kim.hyunsub.photo.util.isVideo
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.HttpClientErrorException
import java.time.LocalDateTime
import kotlin.io.path.Path
import kotlin.io.path.extension

@Authorized(authorities = ["service_photo"])
@RestController
@RequestMapping("/api/v1")
class PhotoUploadController(
	private val albumRepository: AlbumRepository,
	private val apiCaller: ApiCaller,
	private val photoMetadataDateParser: PhotoMetadataDateParser,
	private val photoMetadataRepository: PhotoMetadataRepository,
	private val mapper: ObjectMapper,
	private val photoRepository: PhotoRepository,
	private val apiModelConverter: ApiModelConverter,
	private val thumbnailService: ThumbnailService,
	private val encodeApiCaller: EncodeApiCaller,
	private val fileUrlConverter: FileUrlConverter,
) {
	companion object : Log

	@PostMapping("/albums/{albumId}/photos")
	fun upload(
		@PathVariable albumId: Int,
		@RequestBody params: RestApiPhotoUploadParams,
	): RestApiPhoto {
		val album = albumRepository.findByIdOrNull(albumId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)
		log.debug("[Upload Photo] album={}", album)

		val fileName = params.fileName
		log.debug("[Upload Photo] fileName={}", fileName)

		val isImageFile = isImage(fileName)
		val isVideoFile = isVideo(fileName)

		if (!isVideoFile && !isImageFile) {
			throw ErrorCodeException(ErrorCode.INVALID_PARAMETER)
		}

		val filePath = Path(PhotoConstants.basePath, album.name, fileName).toString()
		log.debug("[Upload Photo] filePath={}", filePath)

		val existPhoto = photoRepository.findByPath(filePath)
		if (existPhoto != null) {
			log.debug("[Upload Photo] existPhoto={}", existPhoto)
			throw ErrorCodeException(ErrorCode.ALREADY_EXIST)
		}

		try {
			val noncePath = fileUrlConverter.getNoncePath(params.nonce)
			apiCaller.rename(noncePath, filePath)
		} catch (ex: HttpClientErrorException) {
			// TODO 이미 존재하면 파일 이름을 다르게 해서 저장하기
			throw ErrorCodeException(ErrorCode.ALREADY_EXIST, mapper.readTree(ex.responseBodyAsString))
		}

		val metadataStr = apiCaller.exif(filePath)
		val metadataNode = mapper.readTree(metadataStr)
		val width = metadataNode[0]["ImageWidth"].asInt()
		val height = metadataNode[0]["ImageHeight"].asInt()
		val size = metadataNode[0]["FileSize"].asInt()
		val metadata = PhotoMetadata(
			path = filePath,
			data = metadataStr,
			date = LocalDateTime.now()
		)

		if (isImageFile) {
			photoMetadataRepository.saveAndFlush(metadata)
		}

		val targetFilePath = if (isImageFile) {
			filePath
		} else {
			val ext = Path(filePath).extension
			filePath.replace(Regex("$ext$"), "mp4")
		}
		log.debug("[Upload Photo] targetFilePath={}", targetFilePath)

		val thumbnailPath = thumbnailService.generateThumbnail(filePath, targetFilePath)
		log.debug("[Upload Photo] thumbnailPath={}", thumbnailPath)

		val photo = Photo(
			path = targetFilePath,
			width = width,
			height = height,
			date = photoMetadataDateParser.determineDate(metadata) ?: LocalDateTime.MIN,
			size = if (isImageFile) size else 0,
			albumId = albumId,
			regDt = LocalDateTime.now(),
		)
		log.debug("Upload Photo : {}", photo)
		photoRepository.saveAndFlush(photo)

		if (isVideoFile) {
			encodeApiCaller.encode(filePath, targetFilePath, photo.id)
		}

		return apiModelConverter.convert(photo)
	}
}
