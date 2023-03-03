package kim.hyunsub.photo.service

import kim.hyunsub.common.api.EncodeApiCaller
import kim.hyunsub.common.api.model.ApiEncodeParams
import kim.hyunsub.common.config.AppProperties
import org.springframework.stereotype.Service

@Service
class PhotoEncodeApiCaller(
	private val encodeApiCaller: EncodeApiCaller,
	private val appProperties: AppProperties,
) {
	fun encode(input: String, output: String, photoId: Int) {
		encodeApiCaller.encode(
			ApiEncodeParams(
				input = input,
				output = output,
				options = "-vcodec libx264 -acodec copy -map_metadata 0 -movflags use_metadata_tags",
				callback = "https://${appProperties.host}/api/v1/encode/callback?photoId=$photoId",
			)
		)
	}
}
