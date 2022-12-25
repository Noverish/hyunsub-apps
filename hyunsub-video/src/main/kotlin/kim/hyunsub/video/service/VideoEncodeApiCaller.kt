package kim.hyunsub.video.service

import kim.hyunsub.common.api.EncodeApiCaller
import kim.hyunsub.common.api.model.ApiEncodeParams
import kim.hyunsub.common.config.AppProperties
import kim.hyunsub.video.repository.entity.Video
import org.springframework.stereotype.Service

@Service
class VideoEncodeApiCaller(
	private val encodeApiCaller: EncodeApiCaller,
	private val appProperties: AppProperties,
) {
	fun encode(video: Video, options: String? = null) {
		val path = video.path

		encodeApiCaller.encode(ApiEncodeParams(
			input = path,
			output = path,
			options = options ?: "-vcodec libx264 -acodec copy",
			callback = "https://${appProperties.host}/api/v1/encode/callback?videoId=${video.id}"
		))
	}
}
