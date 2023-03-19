package kim.hyunsub.photo.service

import kim.hyunsub.common.api.ApiCaller
import kim.hyunsub.common.api.model.ApiPhotoConvertParams
import kim.hyunsub.common.api.model.VideoThumbnailParams
import kim.hyunsub.photo.repository.entity.PhotoV2
import kim.hyunsub.photo.util.PhotoPathUtils
import org.springframework.stereotype.Service

@Service
class ThumbnailServiceV2(
	private val apiCaller: ApiCaller,
) {
	fun generateThumbnail(photo: PhotoV2) {
		val original = PhotoPathUtils.original(photo)
		val thumbnail = PhotoPathUtils.thumbnail(photo.id)

		if (photo.isVideo) {
			apiCaller.videoThumbnail(
				VideoThumbnailParams(
					input = original,
					output = thumbnail,
					time = 0.0,
				)
			)
		} else {
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
		}
	}
}
