package kim.hyunsub.photo.service

import kim.hyunsub.common.fs.client.FsClient
import kim.hyunsub.common.fs.client.FsImageClient
import kim.hyunsub.common.fs.client.FsVideoClient
import kim.hyunsub.common.fs.client.remove
import kim.hyunsub.common.fs.model.ImageConvertParams
import kim.hyunsub.common.fs.model.VideoThumbnailParams
import kim.hyunsub.photo.repository.entity.Photo
import kim.hyunsub.photo.util.PhotoPathConverter
import kim.hyunsub.photo.util.isGif
import kim.hyunsub.photo.util.isImage
import kim.hyunsub.photo.util.isVideo
import org.springframework.stereotype.Service

@Service
class ThumbnailService(
	private val fsClient: FsClient,
	private val fsVideoClient: FsVideoClient,
	private val fsImageClient: FsImageClient,
) {
	fun generateThumbnail(photo: Photo) {
		val original = PhotoPathConverter.original(photo)
		val thumbnail = PhotoPathConverter.thumbnail(photo.id)

		if (isVideo(photo.fileName)) {
			val tmp = "$thumbnail.jpg"

			fsVideoClient.thumbnail(
				VideoThumbnailParams(
					input = original,
					output = tmp,
					time = 0.0,
					sync = true,
				)
			)

			fsImageClient.convert(
				ImageConvertParams(
					input = tmp,
					output = thumbnail,
					resize = "256x256^",
					quality = 60,
					gravity = "center",
					extent = "256x256"
				)
			)

			fsClient.remove(tmp)

			return
		}

		if (isImage(photo.fileName) || isGif(photo.fileName)) {
			fsImageClient.convert(
				ImageConvertParams(
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
