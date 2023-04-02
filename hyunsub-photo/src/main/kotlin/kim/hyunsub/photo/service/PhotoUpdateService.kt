package kim.hyunsub.photo.service

import kim.hyunsub.photo.repository.AlbumPhotoRepository
import kim.hyunsub.photo.repository.PhotoMetadataV2Repository
import kim.hyunsub.photo.repository.PhotoOwnerRepository
import kim.hyunsub.photo.repository.PhotoV2Repository
import mu.KotlinLogging
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class PhotoUpdateService(
	private val photoRepository: PhotoV2Repository,
	private val photoOwnerRepository: PhotoOwnerRepository,
	private val albumPhotoRepository: AlbumPhotoRepository,
	private val photoMetadataRepository: PhotoMetadataV2Repository,
) {
	private val log = KotlinLogging.logger { }

	@Transactional
	fun updateId(from: String, to: String) {
		val photo = photoRepository.updateId(from, to)
		val photoOwner = photoOwnerRepository.updatePhotoId(from, to)
		val albumPhoto = albumPhotoRepository.updatePhotoId(from, to)
		val photoMetadata = photoMetadataRepository.updatePhotoId(from, to)
		log.debug { "[Update Photo Id] photo=$photo, photoOwner=$photoOwner, albumPhoto=$albumPhoto, photoMetadata=$photoMetadata" }
	}
}
