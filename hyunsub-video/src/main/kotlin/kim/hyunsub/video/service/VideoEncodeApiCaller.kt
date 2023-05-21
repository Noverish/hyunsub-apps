package kim.hyunsub.video.service

import kim.hyunsub.common.config.AppProperties
import kim.hyunsub.common.fs.FsEncodeClient
import kim.hyunsub.common.fs.model.EncodeParams
import kim.hyunsub.video.repository.entity.Video
import org.springframework.stereotype.Service

@Service
class VideoEncodeApiCaller(
	private val fsEncodeClient: FsEncodeClient,
	private val appProperties: AppProperties,
) {
	fun encode(video: Video, options: String? = null) {
		val path = video.path

		fsEncodeClient.encode(
			EncodeParams(
				input = path,
				output = path,
				options = options ?: "-vcodec libx264 -acodec copy",
				callback = "https://${appProperties.host}/api/v1/encode/callback?videoId=${video.id}"
			)
		)
	}
}
