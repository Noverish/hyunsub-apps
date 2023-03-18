package kim.hyunsub.photo.service

import kim.hyunsub.common.api.ApiCaller
import kim.hyunsub.common.api.model.ApiPhotoConvertParams
import kim.hyunsub.common.api.model.VideoThumbnailParams
import kim.hyunsub.photo.util.PhotoPathUtils
import kim.hyunsub.photo.util.isImage
import kim.hyunsub.photo.util.isVideo
import org.springframework.stereotype.Service

@Service
class ThumbnailServiceV2(
	private val apiCaller: ApiCaller,
) {
	fun generateThumbnail(file: String, year: Int) {
		val original = PhotoPathUtils.original(file, year)
		val thumbnail = PhotoPathUtils.thumbnail(file, year)

		if (isVideo(file)) {
			apiCaller.videoThumbnail(
				VideoThumbnailParams(
					input = original,
					output = thumbnail,
					time = 0.0,
				)
			)
			return
		}

		if (isImage(file)) {
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

		throw RuntimeException()
	}
}
