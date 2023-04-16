package kim.hyunsub.photo.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kim.hyunsub.common.api.ApiCaller
import kim.hyunsub.common.util.decodeHex
import kim.hyunsub.common.util.toBase64
import kim.hyunsub.photo.model.api.ApiPhotoUploadParams
import kim.hyunsub.photo.repository.AlbumPhotoRepository
import kim.hyunsub.photo.repository.AlbumRepository
import kim.hyunsub.photo.repository.PhotoMetadataRepository
import kim.hyunsub.photo.repository.PhotoOwnerRepository
import kim.hyunsub.photo.repository.PhotoRepository
import kim.hyunsub.photo.repository.entity.AlbumPhoto
import kim.hyunsub.photo.repository.entity.AlbumPhotoId
import kim.hyunsub.photo.repository.entity.Photo
import kim.hyunsub.photo.repository.entity.PhotoMetadata
import kim.hyunsub.photo.repository.entity.PhotoOwner
import kim.hyunsub.photo.repository.entity.PhotoOwnerId
import kim.hyunsub.photo.repository.generateId
import kim.hyunsub.photo.util.PhotoDateParser
import kim.hyunsub.photo.util.PhotoPathUtils
import kim.hyunsub.photo.util.isImage
import kim.hyunsub.photo.util.isVideo
import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.io.path.Path
import kotlin.io.path.extension
import kotlin.io.path.nameWithoutExtension
import kotlin.math.abs

@Service
class PhotoUploadService(
	private val apiCaller: ApiCaller,
	private val encodeApiCaller: PhotoEncodeApiCaller,
	private val photoRepository: PhotoRepository,
	private val thumbnailService: ThumbnailService,
	private val photoOwnerRepository: PhotoOwnerRepository,
	private val albumPhotoRepository: AlbumPhotoRepository,
	private val albumRepository: AlbumRepository,
	private val photoMetadataRepository: PhotoMetadataRepository,
) {
	private val log = KotlinLogging.logger { }
	private val mapper = jacksonObjectMapper()

	fun upload(userId: String, params: ApiPhotoUploadParams): Photo {
		val photo = getOrCreatePhoto(params)

		val photoOwner = getOrCreatePhotoOwner(userId, photo, params)

		params.albumId?.let {
			getOrCreateAlbumPhoto(userId, photo, it)
		}

		pairingPhoto(userId, photo, photoOwner)

		return photo
	}

	private fun getOrCreatePhoto(params: ApiPhotoUploadParams): Photo {
		val tmpPath = PhotoPathUtils.tmp(params.nonce)

		val hash = apiCaller.hash(tmpPath).result.decodeHex().toBase64()

		val exist = photoRepository.findByHash(hash)
		if (exist != null) {
			log.debug { "[PhotoUpload] Already exist photo: $exist" }
			apiCaller.remove(tmpPath)
			return exist
		}

		val exif = mapper.readTree(apiCaller.exif(tmpPath))[0]
		val parseResult = PhotoDateParser.parse(exif, params.name, params.millis)
		val date = parseResult.date
		val dateType = parseResult.type
		val id = photoRepository.generateId(date, hash)

		val photo = Photo(
			id = id,
			hash = hash,
			width = exif["ImageWidth"].asInt(),
			height = exif["ImageHeight"].asInt(),
			size = exif["FileSize"].asInt(),
			offset = date.offset.totalSeconds,
			ext = Path(params.name).extension,
			dateType = dateType,
		)

		// move photo to original folder
		val originalPath = PhotoPathUtils.original(photo)
		apiCaller.rename(tmpPath, originalPath)

		// generate thumbnail
		thumbnailService.generateThumbnail(photo)
		if (photo.isVideo) {
			val videoPath = PhotoPathUtils.video(id)
			encodeApiCaller.encode(
				input = originalPath,
				output = videoPath,
				photoId = id,
			)
		}

		// save photo
		photoRepository.save(photo)

		// save metadata
		photoMetadataRepository.save(PhotoMetadata.from(id, exif))

		return photo
	}

	private fun getOrCreatePhotoOwner(userId: String, photo: Photo, params: ApiPhotoUploadParams): PhotoOwner {
		val exist = photoOwnerRepository.findByIdOrNull(PhotoOwnerId(userId, photo.id))
		if (exist != null) {
			log.debug { "[PhotoUpload] Already exist photo owner: $exist" }
			return exist
		}

		val photoOwner = PhotoOwner(
			userId = userId,
			photoId = photo.id,
			name = params.name,
			fileDt = LocalDateTime.ofInstant(Instant.ofEpochMilli(params.millis), ZoneId.systemDefault()),
			regDt = LocalDateTime.now(),
		)
		photoOwnerRepository.save(photoOwner)

		return photoOwner
	}

	private fun getOrCreateAlbumPhoto(userId: String, photo: Photo, albumId: String): AlbumPhoto {
		val exist = albumPhotoRepository.findByIdOrNull(AlbumPhotoId(albumId, photo.id))
		if (exist != null) {
			log.debug { "[PhotoUpload] Already exist album photo: $exist" }
			return exist
		}

		val albumPhoto = AlbumPhoto(
			albumId = albumId,
			photoId = photo.id,
			userId = userId,
		)
		albumPhotoRepository.save(albumPhoto)

		val album = albumRepository.findByIdOrNull(albumId)
		if (album != null && album.thumbnailPhotoId == null) {
			albumRepository.save(album.copy(thumbnailPhotoId = photo.id))
		}

		return albumPhoto
	}

	private fun pairingPhoto(userId: String, photo: Photo, photoOwner: PhotoOwner) {
		if (photo.pairPhotoId != null) {
			log.debug { "[PhotoUpload Pairing] Photo is already paired: ${photo.id} - ${photo.pairPhotoId}" }
			return
		}

		val nameWithoutExt = Path(photoOwner.name).nameWithoutExtension
		val candidates = photoOwnerRepository.selectMyPhotoByName(userId, nameWithoutExt)

		val candidates2 = when {
			isVideo(photoOwner.name) -> candidates.filter { isImage(it.name) }
			isImage(photoOwner.name) -> candidates.filter { isVideo(it.name) }
			else -> run {
				log.debug { "[PhotoUpload Pairing] Unsupported type of pair photo: ${photoOwner.name}" }
				return
			}
		}

		if (candidates2.size != 1) {
			log.debug { "[PhotoUpload Pairing] Candidate size is not 1: $candidates2" }
			return
		}

		val pairPhoto = photoRepository.findByIdOrNull(candidates2.first().photoId) ?: return
		if (abs(photo.millis - pairPhoto.millis) > 3000) {
			log.debug { "[PhotoUpload Pairing] Different date of pair photo: ${photo.date}, ${pairPhoto.date}" }
			return
		}

		if (pairPhoto.pairPhotoId != null) {
			log.debug { "[PhotoUpload Pairing] Candidate photo is already paired: ${pairPhoto.id} - ${pairPhoto.pairPhotoId}" }
			return
		}

		log.debug { "[PhotoUpload Pairing] Successfully pairing photo: ${photo.id} - ${pairPhoto.id}" }
		photoRepository.save(photo.copy(pairPhotoId = pairPhoto.id))
		photoRepository.save(pairPhoto.copy(pairPhotoId = photo.id))
	}
}
