package kim.hyunsub.photo.service

import kim.hyunsub.common.api.EncodeApiCaller
import kim.hyunsub.common.api.model.EncodeParams
import kim.hyunsub.photo.config.PhotoProperties
import org.springframework.stereotype.Service

@Service
class PhotoEncodeApiCaller(
	private val encodeApiCaller: EncodeApiCaller,
	private val photoProperties: PhotoProperties,
) {
	fun encode(input: String, output: String, photoId: Int) {
		encodeApiCaller.encode(EncodeParams(
			input = input,
			output = output,
			options = "-vcodec libx264 -acodec copy -map_metadata 0 -movflags use_metadata_tags",
			callback = "https://${photoProperties.host}/api/v1/encode/callback?photoId=$photoId",
		))
	}
}
