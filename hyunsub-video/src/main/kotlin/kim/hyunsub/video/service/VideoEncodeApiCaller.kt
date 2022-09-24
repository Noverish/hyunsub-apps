package kim.hyunsub.video.service

import kim.hyunsub.common.api.EncodeApiCaller
import kim.hyunsub.common.api.model.EncodeParams
import kim.hyunsub.video.config.VideoProperties
import kim.hyunsub.video.repository.entity.Video
import org.springframework.stereotype.Service

@Service
class VideoEncodeApiCaller(
	private val encodeApiCaller: EncodeApiCaller,
	private val videoProperties: VideoProperties,
) {
	fun encode(video: Video, options: String? = null) {
		val path = video.path

		encodeApiCaller.encode(EncodeParams(
			input = path,
			output = path,
			options = options ?: "-vcodec libx264 -acodec copy",
			callback = "https://${videoProperties.host}/api/v1/encode/callback?videoId=${video.id}"
		))
	}
}
