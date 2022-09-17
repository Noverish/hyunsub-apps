package kim.hyunsub.photo.service

import kim.hyunsub.common.api.ApiProperties
import kim.hyunsub.common.http.HttpClient
import kim.hyunsub.common.web.config.WebConstants
import kim.hyunsub.photo.config.PhotoProperties
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service

@Service
class EncodeApiCaller(
	private val httpClient: HttpClient,
	private val apiProperties: ApiProperties,
	private val photoProperties: PhotoProperties,
) {
	fun encode(input: String, output: String, photoId: Int) {
		val url = "https://encode.hyunsub.kim/api/v1/encode"
		val body = mapOf(
			"input" to input,
			"output" to output,
			"options" to "-vcodec libx264 -acodec copy -map_metadata 0 -movflags use_metadata_tags",
			"callback" to "https://${photoProperties.host}/api/v1/encode/callback?photoId=$photoId"
		)
		val cookie = "${WebConstants.TOKEN_COOKIE_NAME}=${apiProperties.token}"
		val headers = buildMap {
			this += (HttpHeaders.CONTENT_TYPE to MediaType.APPLICATION_JSON_VALUE)
			this += (HttpHeaders.COOKIE to cookie)
		}
		httpClient.post<String>(url, body = body, headers = headers)
	}
}
