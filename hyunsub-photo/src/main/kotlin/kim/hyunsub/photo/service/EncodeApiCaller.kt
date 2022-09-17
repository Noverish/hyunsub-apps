package kim.hyunsub.photo.service

import kim.hyunsub.common.api.ApiProperties
import kim.hyunsub.common.http.HttpClient
import kim.hyunsub.common.web.config.WebConstants
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service

@Service
class EncodeApiCaller(
	private val httpClient: HttpClient,
	private val apiProperties: ApiProperties,
) {
	fun encode(input: String) {
		val url = "https://encode.hyunsub.kim/api/v1/encode"
		val body = mapOf(
			"input" to input,
			"options" to "-vcodec libx264 -acodec copy"
		)
		val cookie = "${WebConstants.TOKEN_COOKIE_NAME}=${apiProperties.token}"
		val headers = buildMap {
			this += (HttpHeaders.CONTENT_TYPE to MediaType.APPLICATION_JSON_VALUE)
			this += (HttpHeaders.COOKIE to cookie)
		}
		httpClient.post<String>(url, body = body, headers = headers)
	}
}
