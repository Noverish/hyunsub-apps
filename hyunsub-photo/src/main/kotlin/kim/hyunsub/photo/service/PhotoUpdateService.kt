package kim.hyunsub.photo.service

import jakarta.transaction.Transactional
import kim.hyunsub.common.fs.client.FsClient
import kim.hyunsub.common.fs.client.rename
import kim.hyunsub.photo.repository.condition.AlbumCondition
import kim.hyunsub.photo.repository.entity.Photo
import kim.hyunsub.photo.repository.mapper.AlbumMapper
import kim.hyunsub.photo.repository.mapper.AlbumPhotoMapper
import kim.hyunsub.photo.repository.mapper.PhotoMapper
import kim.hyunsub.photo.repository.mapper.PhotoMetadataMapper
import kim.hyunsub.photo.repository.mapper.PhotoOwnerMapper
import kim.hyunsub.photo.util.PhotoPathConverter
import kim.hyunsub.photo.util.isVideo
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class PhotoUpdateService(
	private val fsClient: FsClient,
	private val photoOwnerMapper: PhotoOwnerMapper,
	private val albumMapper: AlbumMapper,
	private val photoMetadataMapper: PhotoMetadataMapper,
	private val photoMapper: PhotoMapper,
	private val albumPhotoMapper: AlbumPhotoMapper,
) {
	private val log = KotlinLogging.logger { }

	@Transactional
	fun updateId(photo: Photo, newId: String): Photo {
		val oldId = photo.id
		val newPhoto = photo.copy(id = newId)

		val oldOriginalPath = PhotoPathConverter.originalNew(photo)
		val newOriginalPath = PhotoPathConverter.originalNew(newPhoto)
		fsClient.rename(oldOriginalPath, newOriginalPath)

		val oldThumbnailPath = PhotoPathConverter.thumbnailNew(photo)
		val newThumbnailPath = PhotoPathConverter.thumbnailNew(newPhoto)
		fsClient.rename(oldThumbnailPath, newThumbnailPath)

		if (isVideo(photo.fileName)) {
			val oldVideoPath = PhotoPathConverter.videoNew(photo)
			val newVideoPath = PhotoPathConverter.videoNew(newPhoto)
			fsClient.rename(oldVideoPath, newVideoPath)
		}

		val albums = albumMapper.select(AlbumCondition(thumbnailPhotoId = oldId))
		for (album in albums) {
			val newAlbum = album.copy(thumbnailPhotoId = newId)
			albumMapper.insert(newAlbum)
		}

		val photoResult = photoMapper.updateId(from = oldId, to = newId)
		val photoOwner = photoOwnerMapper.updatePhotoId(oldId, newId)
		val albumPhoto = albumPhotoMapper.updatePhotoId(oldId, newId)
		val photoMetadata = photoMetadataMapper.updatePhotoId(oldId, newId)

		log.debug { "[Update Photo Id] albums=${albums.size} photo=$photoResult, photoOwner=$photoOwner, albumPhoto=$albumPhoto, photoMetadata=$photoMetadata" }

		return newPhoto
	}
}
