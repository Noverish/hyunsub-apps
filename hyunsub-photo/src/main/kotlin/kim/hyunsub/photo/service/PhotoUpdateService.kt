package kim.hyunsub.photo.service

import kim.hyunsub.common.api.ApiCaller
import kim.hyunsub.photo.repository.AlbumPhotoRepository
import kim.hyunsub.photo.repository.AlbumV2Repository
import kim.hyunsub.photo.repository.PhotoMetadataV2Repository
import kim.hyunsub.photo.repository.PhotoOwnerRepository
import kim.hyunsub.photo.repository.PhotoV2Repository
import kim.hyunsub.photo.repository.entity.PhotoV2
import kim.hyunsub.photo.util.PhotoPathUtils
import mu.KotlinLogging
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class PhotoUpdateService(
	private val apiCaller: ApiCaller,
	private val photoRepository: PhotoV2Repository,
	private val photoOwnerRepository: PhotoOwnerRepository,
	private val albumPhotoRepository: AlbumPhotoRepository,
	private val albumRepository: AlbumV2Repository,
	private val photoMetadataRepository: PhotoMetadataV2Repository,
) {
	private val log = KotlinLogging.logger { }

	@Transactional
	fun updateId(photo: PhotoV2, newId: String) {
		val oldId = photo.id
		val newPhoto = photo.copy(id = newId)

		val oldOriginalPath = PhotoPathUtils.original(photo)
		val newOriginalPath = PhotoPathUtils.original(newPhoto)
		apiCaller.rename(oldOriginalPath, newOriginalPath)

		val oldThumbnailPath = PhotoPathUtils.thumbnail(oldId)
		val newThumbnailPath = PhotoPathUtils.thumbnail(newId)
		apiCaller.rename(oldThumbnailPath, newThumbnailPath)

		if (photo.isVideo) {
			val oldVideoPath = PhotoPathUtils.video(oldId)
			val newVideoPath = PhotoPathUtils.video(newId)
			apiCaller.rename(oldVideoPath, newVideoPath)
		}

		val albums = albumRepository.findByThumbnailPhotoId(oldId)
		for (album in albums) {
			val newAlbum = album.copy(thumbnailPhotoId = newId)
			albumRepository.save(newAlbum)
		}

		val photoResult = photoRepository.updateId(oldId, newId)
		val photoOwner = photoOwnerRepository.updatePhotoId(oldId, newId)
		val albumPhoto = albumPhotoRepository.updatePhotoId(oldId, newId)
		val photoMetadata = photoMetadataRepository.updatePhotoId(oldId, newId)

		log.debug { "[Update Photo Id] albums=${albums.size} photo=$photoResult, photoOwner=$photoOwner, albumPhoto=$albumPhoto, photoMetadata=$photoMetadata" }
	}
}
