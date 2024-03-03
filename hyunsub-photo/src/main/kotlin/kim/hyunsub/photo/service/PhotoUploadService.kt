package kim.hyunsub.photo.service

import com.fasterxml.jackson.databind.JsonNode
import kim.hyunsub.common.fs.FsPathConverter
import kim.hyunsub.common.fs.client.FsClient
import kim.hyunsub.common.fs.client.FsImageClient
import kim.hyunsub.common.fs.client.remove
import kim.hyunsub.common.fs.client.rename
import kim.hyunsub.common.util.decodeHex
import kim.hyunsub.common.util.toBase64
import kim.hyunsub.common.util.toUtcLdt
import kim.hyunsub.photo.model.api.ApiPhotoUploadParams
import kim.hyunsub.photo.repository.entity.AlbumPhoto
import kim.hyunsub.photo.repository.entity.Photo
import kim.hyunsub.photo.repository.entity.PhotoMetadata
import kim.hyunsub.photo.repository.entity.PhotoOwner
import kim.hyunsub.photo.repository.entity.PhotoPreview
import kim.hyunsub.photo.repository.mapper.AlbumMapper
import kim.hyunsub.photo.repository.mapper.AlbumPhotoMapper
import kim.hyunsub.photo.repository.mapper.PhotoMapper
import kim.hyunsub.photo.repository.mapper.PhotoMetadataMapper
import kim.hyunsub.photo.repository.mapper.PhotoOwnerMapper
import kim.hyunsub.photo.repository.mapper.generateId
import kim.hyunsub.photo.util.PhotoDateParser
import kim.hyunsub.photo.util.PhotoPathConverter
import kim.hyunsub.photo.util.isVideo
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import kotlin.io.path.Path
import kotlin.io.path.extension

@Service
class PhotoUploadService(
	private val fsClient: FsClient,
	private val fsImageClient: FsImageClient,
	private val encodeApiCaller: PhotoEncodeApiCaller,
	private val thumbnailService: ThumbnailService,
	private val photoOwnerMapper: PhotoOwnerMapper,
	private val albumMapper: AlbumMapper,
	private val photoMetadataMapper: PhotoMetadataMapper,
	private val photoMapper: PhotoMapper,
	private val albumPhotoMapper: AlbumPhotoMapper,
) {
	private val log = KotlinLogging.logger { }

	fun upload(userId: String, params: ApiPhotoUploadParams): PhotoPreview {
		val noncePath = FsPathConverter.noncePath(params.nonce)
		val exif = fsImageClient.exif(noncePath)[0]

		val photo = getOrCreatePhoto(params, noncePath, exif)

		val photoOwner = getOrCreatePhotoOwner(userId, photo.id, params, exif)

		params.albumId?.let {
			getOrCreateAlbumPhoto(userId, photo.id, it)
		}

		return PhotoPreview(photo, photoOwner)
	}

	private fun getOrCreatePhoto(params: ApiPhotoUploadParams, noncePath: String, exif: JsonNode): Photo {
		val hash = fsClient.hash(noncePath).result.decodeHex().toBase64()

		val exist = photoMapper.selectByHash(hash)
		if (exist != null) {
			log.debug { "[PhotoUpload] Already exist photo: hash=$hash" }
			fsClient.remove(noncePath)
			return exist
		}

		val photo = Photo(
			id = photoMapper.generateId(),
			hash = hash,
			width = exif["ImageWidth"].asInt(),
			height = exif["ImageHeight"].asInt(),
			size = exif["FileSize"].asInt(),
			ext = Path(params.name).extension,
		)
		log.debug { "[PhotoUpload] Generated ID: ${photo.id}" }

		// move photo to original folder
		val originalPath = PhotoPathConverter.original(photo)
		fsClient.rename(noncePath, originalPath, true)

		// generate thumbnail
		thumbnailService.generateThumbnail(photo)
		if (isVideo(photo.fileName)) {
			val videoPath = PhotoPathConverter.video(photo)
			encodeApiCaller.encode(
				input = originalPath,
				output = videoPath,
				photoId = photo.id,
			)
		}

		// save photo
		photoMapper.insert(photo)

		// save metadata
		photoMetadataMapper.upsert(PhotoMetadata.from(photo.id, exif))

		return photo
	}

	private fun getOrCreatePhotoOwner(userId: String, photoId: String, params: ApiPhotoUploadParams, exif: JsonNode): PhotoOwner {
		val exist = photoOwnerMapper.selectOne(userId = userId, photoId = photoId)
		if (exist != null) {
			log.debug { "[PhotoUpload] Already exist photo owner: $exist" }
			return exist
		}

		val parseResult = PhotoDateParser.parse(exif, params.name, params.millis)

		val photoOwner = PhotoOwner(
			userId = userId,
			photoId = photoId,
			name = params.name,
			fileEpoch = (params.millis / 1000).toInt(),
			regDt = LocalDateTime.now(),
			date = parseResult.date.toUtcLdt(),
			offset = parseResult.date.offset.totalSeconds,
			dateType = parseResult.type,
		)

		photoOwnerMapper.insert(photoOwner)

		return photoOwner
	}

	private fun getOrCreateAlbumPhoto(userId: String, photoId: String, albumId: String): AlbumPhoto? {
		val exist = albumPhotoMapper.selectOne(albumId = albumId, photoId = photoId)
		if (exist != null) {
			log.debug { "[PhotoUpload] Already exist album photo: $exist" }
			return exist
		}

		val album = albumMapper.selectWithUserId(albumId = albumId, userId = userId)
		if (album == null) {
			log.warn { "[PhotoUpdate] No such album: albumId=$albumId" }
			return null
		}

		val albumPhoto = AlbumPhoto(
			albumId = albumId,
			photoId = photoId,
			userId = userId,
		)

		albumPhotoMapper.insert(albumPhoto)

		if (album.thumbnailPhotoId == null) {
			albumMapper.update(album.copy(thumbnailPhotoId = photoId))
		}

		return albumPhoto
	}
}
