package kim.hyunsub.photo.service

import kim.hyunsub.common.api.EncodeApiCaller
import kim.hyunsub.common.api.model.EncodeParams
import kim.hyunsub.common.config.AppProperties
import org.springframework.stereotype.Service

@Service
class PhotoEncodeApiCaller(
	private val encodeApiCaller: EncodeApiCaller,
	private val appProperties: AppProperties,
) {
	fun encode(input: String, output: String, photoId: String) {
		encodeApiCaller.encode(
			EncodeParams(
				input = input,
				output = output,
				options = "-vcodec libx264 -crf 33 -acodec copy",
				callback = "https://${appProperties.host}/api/v1/encode/callback?photoId=$photoId",
			)
		)
	}
}
