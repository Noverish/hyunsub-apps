package kim.hyunsub.photo.service

import kim.hyunsub.common.fs.FsPathConverter
import kim.hyunsub.common.util.getHumanReadableSize
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.photo.model.api.ApiPhoto
import kim.hyunsub.photo.repository.AlbumOwnerRepository
import kim.hyunsub.photo.repository.AlbumPhotoRepository
import kim.hyunsub.photo.repository.PhotoOwnerRepository
import kim.hyunsub.photo.repository.PhotoRepository
import kim.hyunsub.photo.repository.entity.AlbumOwnerId
import kim.hyunsub.photo.repository.entity.AlbumPhotoId
import kim.hyunsub.photo.repository.entity.PhotoOwnerId
import kim.hyunsub.photo.util.PhotoPathConverter
import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class PhotoDetailService(
	private val photoRepository: PhotoRepository,
	private val photoOwnerRepository: PhotoOwnerRepository,
	private val albumOwnerRepository: AlbumOwnerRepository,
	private val albumPhotoRepository: AlbumPhotoRepository,
) {
	private val log = KotlinLogging.logger { }

	fun detail(userId: String, photoId: String): ApiPhoto {
		val photoOwner = photoOwnerRepository.findByIdOrNull(PhotoOwnerId(userId, photoId))
			?: run {
				log.debug { "[Detail Photo] No such photo owner: userId=$userId, photoId=$photoId" }
				throw ErrorCodeException(ErrorCode.NOT_FOUND)
			}

		val photo = photoRepository.findByIdOrNull(photoId)
			?: run {
				log.error { "[Detail Photo] No such photo: $photoId" }
				throw ErrorCodeException(ErrorCode.INTERNAL_SERVER_ERROR)
			}

		return ApiPhoto(
			id = photo.id,
			imageSize = "${photo.width} x ${photo.height}",
			fileSize = getHumanReadableSize(photo.size.toLong()),
			date = photo.date,
			regDt = photoOwner.regDt,
			fileName = photoOwner.name,
			dateType = photo.dateType,
			original = FsPathConverter.convertToUrl(PhotoPathConverter.original(photo))
		)
	}

	fun detailInAlbum(userId: String, albumId: String, photoId: String): ApiPhoto {
		albumOwnerRepository.findByIdOrNull(AlbumOwnerId(albumId, userId))
			?: run {
				log.debug { "[Detail Photo] No such album owner: userId=$userId, albumId=$albumId" }
				throw ErrorCodeException(ErrorCode.NOT_FOUND)
			}

		val albumPhoto = albumPhotoRepository.findByIdOrNull(AlbumPhotoId(albumId, photoId))
			?: run {
				log.debug { "[Detail Photo] No such album photo: photoId=$photoId, albumId=$albumId" }
				throw ErrorCodeException(ErrorCode.NOT_FOUND)
			}

		return detail(albumPhoto.userId, photoId)
	}
}
