package kim.hyunsub.photo.service

import kim.hyunsub.common.config.AppProperties
import kim.hyunsub.common.fs.FsEncodeClient
import kim.hyunsub.common.fs.model.EncodeParams
import org.springframework.stereotype.Service

@Service
class PhotoEncodeApiCaller(
	private val fsEncodeClient: FsEncodeClient,
	private val appProperties: AppProperties,
) {
	fun encode(input: String, output: String, photoId: String) {
		fsEncodeClient.encode(
			EncodeParams(
				input = input,
				output = output,
				options = "-vcodec libx264 -crf 33 -pix_fmt yuv420p -acodec copy -y",
				callback = "https://${appProperties.host.replace("local-", "")}/api/v1/encode/callback?photoId=$photoId",
			)
		)
	}
}
