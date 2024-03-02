package kim.hyunsub.photo.service

import kim.hyunsub.common.fs.FsPathConverter
import kim.hyunsub.common.fs.client.FsClient
import kim.hyunsub.common.fs.client.FsImageClient
import kim.hyunsub.common.fs.client.remove
import kim.hyunsub.common.fs.client.rename
import kim.hyunsub.common.util.decodeHex
import kim.hyunsub.common.util.toBase64
import kim.hyunsub.common.util.toLdt
import kim.hyunsub.common.util.toUtcLdt
import kim.hyunsub.photo.model.api.ApiPhotoUploadParams
import kim.hyunsub.photo.repository.condition.PhotoCondition
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
import kim.hyunsub.photo.repository.mapper.generateIdOld
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
		val photo = getOrCreatePhoto(params)

		val tmpPath = FsPathConverter.noncePath(params.nonce)
		val exif = fsImageClient.exif(tmpPath)[0]
		val parseResult = PhotoDateParser.parse(exif, params.name, params.millis)
		val photoOwner = getOrCreatePhotoOwner(userId, photo, params, parseResult)

		params.albumId?.let {
			getOrCreateAlbumPhoto(userId, photo, it)
		}

		return PhotoPreview(photo, photoOwner)
	}

	private fun getOrCreatePhoto(params: ApiPhotoUploadParams): Photo {
		val tmpPath = FsPathConverter.noncePath(params.nonce)

		val hash = fsClient.hash(tmpPath).result.decodeHex().toBase64()

		val exist = photoMapper.select(PhotoCondition(hash = hash)).firstOrNull()
		if (exist != null) {
			log.debug { "[PhotoUpload] Already exist photo: $exist" }
			fsClient.remove(tmpPath)
			return exist
		}

		val exif = fsImageClient.exif(tmpPath)[0]
		val parseResult = PhotoDateParser.parse(exif, params.name, params.millis)
		val date = parseResult.date
		val dateType = parseResult.type
		val id = photoMapper.generateIdOld(date, hash)

		val photo = Photo(
			id = id,
			hash = hash,
			width = exif["ImageWidth"].asInt(),
			height = exif["ImageHeight"].asInt(),
			size = exif["FileSize"].asInt(),
			offset = date.offset.totalSeconds,
			ext = Path(params.name).extension,
			dateType = dateType,
			idNew = photoMapper.generateId(),
		)

		// move photo to original folder
		val originalPath = PhotoPathConverter.originalNew(photo)
		fsClient.rename(tmpPath, originalPath, true)

		// generate thumbnail
		thumbnailService.generateThumbnail(photo)
		if (isVideo(photo.fileName)) {
			val videoPath = PhotoPathConverter.videoNew(photo)
			encodeApiCaller.encode(
				input = originalPath,
				output = videoPath,
				photoId = id,
			)
		}

		// save photo
		photoMapper.insert(photo)

		// save metadata
		photoMetadataMapper.upsert(PhotoMetadata.from(id, exif, photo.idNew))

		return photo
	}

	private fun getOrCreatePhotoOwner(userId: String, photo: Photo, params: ApiPhotoUploadParams, parseResult: PhotoDateParser.Result): PhotoOwner {
		val exist = photoOwnerMapper.selectOne(userId = userId, photoId = photo.id)
		if (exist != null) {
			log.debug { "[PhotoUpload] Already exist photo owner: $exist" }
			return exist
		}

		val photoOwner = PhotoOwner(
			userId = userId,
			photoId = photo.id,
			name = params.name,
			fileDt = params.millis.toLdt(),
			regDt = LocalDateTime.now(),
			date = parseResult.date.toUtcLdt(),
			offset = parseResult.date.offset.totalSeconds,
			dateType = parseResult.type,
			photoIdNew = photo.idNew,
		)
		photoOwnerMapper.insert(photoOwner)

		return photoOwner
	}

	private fun getOrCreateAlbumPhoto(userId: String, photo: Photo, albumId: String): AlbumPhoto {
		val exist = albumPhotoMapper.selectOne(albumId = albumId, photoId = photo.id)
		if (exist != null) {
			log.debug { "[PhotoUpload] Already exist album photo: $exist" }
			return exist
		}

		val albumPhoto = AlbumPhoto(
			albumId = albumId,
			photoId = photo.id,
			userId = userId,
		)
		albumPhotoMapper.insert(albumPhoto)

		val album = albumMapper.selectOne(albumId)
		if (album != null && album.thumbnailPhotoId == null) {
			albumMapper.insert(album.copy(thumbnailPhotoId = photo.id))
		}

		return albumPhoto
	}
}
