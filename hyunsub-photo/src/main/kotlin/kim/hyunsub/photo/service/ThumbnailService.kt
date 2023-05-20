package kim.hyunsub.photo.service

import kim.hyunsub.common.api.ApiCaller
import kim.hyunsub.common.api.model.ApiPhotoConvertParams
import kim.hyunsub.common.api.model.VideoThumbnailParams
import kim.hyunsub.common.fs.FsVideoClient
import kim.hyunsub.photo.repository.entity.Photo
import kim.hyunsub.photo.util.PhotoPathUtils
import kim.hyunsub.photo.util.isGif
import kim.hyunsub.photo.util.isImage
import kim.hyunsub.photo.util.isVideo
import org.springframework.stereotype.Service

@Service
class ThumbnailService(
	private val apiCaller: ApiCaller,
	private val fsVideoClient: FsVideoClient,
) {
	fun generateThumbnail(photo: Photo) {
		val original = PhotoPathUtils.original(photo)
		val thumbnail = PhotoPathUtils.thumbnail(photo.id)

		if (isVideo(photo.fileName)) {
			val tmp = "$thumbnail.jpg"

			fsVideoClient.videoThumbnail(
				VideoThumbnailParams(
					input = original,
					output = tmp,
					time = 0.0,
				)
			)

			apiCaller.imageConvert(
				ApiPhotoConvertParams(
					input = tmp,
					output = thumbnail,
					resize = "256x256^",
					quality = 60,
					gravity = "center",
					extent = "256x256"
				)
			)

			apiCaller.remove(tmp)

			return
		}

		if (isImage(photo.fileName) || isGif(photo.fileName)) {
			apiCaller.imageConvert(
				ApiPhotoConvertParams(
					input = original,
					output = thumbnail,
					resize = "256x256^",
					quality = 60,
					gravity = "center",
					extent = "256x256"
				)
			)
			return
		}
	}
}
