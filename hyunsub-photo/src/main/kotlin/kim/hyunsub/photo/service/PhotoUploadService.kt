package kim.hyunsub.photo.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kim.hyunsub.common.api.ApiCaller
import kim.hyunsub.common.util.decodeHex
import kim.hyunsub.common.util.toBase64
import kim.hyunsub.photo.model.dto.PhotoUploadParams
import kim.hyunsub.photo.repository.PhotoOwnerRepository
import kim.hyunsub.photo.repository.PhotoV2Repository
import kim.hyunsub.photo.repository.entity.PhotoOwner
import kim.hyunsub.photo.repository.entity.PhotoOwnerId
import kim.hyunsub.photo.repository.entity.PhotoV2
import kim.hyunsub.photo.repository.generateId
import kim.hyunsub.photo.util.PhotoDateParser
import kim.hyunsub.photo.util.PhotoPathUtils
import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.io.path.Path
import kotlin.io.path.extension

@Service
class PhotoUploadService(
	private val apiCaller: ApiCaller,
	private val photoV2Repository: PhotoV2Repository,
	private val thumbnailServiceV2: ThumbnailServiceV2,
	private val photoOwnerRepository: PhotoOwnerRepository,
) {
	private val log = KotlinLogging.logger { }
	private val mapper = jacksonObjectMapper()

	fun upload(userId: String, params: PhotoUploadParams): PhotoV2 {
		val tmpPath = PhotoPathUtils.tmp(params.nonce)

		val hash = apiCaller.hash(tmpPath).result.decodeHex().toBase64()
		log.debug { "[PhotoUpload] hash=$hash" }

		// 이미 동일한 사진이 업로드 되어 있는 경우
		val exist = photoV2Repository.findByHash(hash)
		if (exist != null) {
			val existOwner = photoOwnerRepository.findByIdOrNull(PhotoOwnerId(userId, exist.id))
			if (existOwner == null) {
				val photoOwner = PhotoOwner(
					userId = userId,
					photoId = exist.id,
					name = params.name,
					regDt = LocalDateTime.now(),
				)
				photoOwnerRepository.save(photoOwner)
			}
			apiCaller.remove(tmpPath)
			return exist
		}

		val exif = mapper.readTree(apiCaller.exif(tmpPath))[0]
		val date = PhotoDateParser.parse(exif)
		log.debug { "[PhotoUpload] date=$date" }

		val id = photoV2Repository.generateId(date, hash)
		log.debug { "[PhotoUpload] id=$id" }

		val ext = Path(params.name).extension
		val year = date.withOffsetSameInstant(ZoneOffset.UTC).year
		val newFile = "$id.$ext"
		val newPath = PhotoPathUtils.original(newFile, year)
		log.debug { "[PhotoUpload] newPath=$newPath" }

		apiCaller.rename(tmpPath, newPath)

		thumbnailServiceV2.generateThumbnail(newFile, year)

		val photo = PhotoV2(
			id = id,
			hash = hash,
			width = exif["ImageWidth"].asInt(),
			height = exif["ImageHeight"].asInt(),
			size = exif["FileSize"].asInt(),
			offset = date.offset.totalSeconds,
			ext = ext,
		)
		photoV2Repository.save(photo)

		val photoOwner = PhotoOwner(
			userId = userId,
			photoId = id,
			name = params.name,
			regDt = LocalDateTime.now(),
		)
		photoOwnerRepository.save(photoOwner)

		return photo
	}
}
