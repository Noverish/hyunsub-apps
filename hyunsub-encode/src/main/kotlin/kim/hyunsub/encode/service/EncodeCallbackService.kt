package kim.hyunsub.encode.service

import kim.hyunsub.common.fs.FsProperties
import kim.hyunsub.common.web.config.WebConstants
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class EncodeCallbackService(
	private val fsProperties: FsProperties,
) {
	private val restTemplate = RestTemplate()

	fun sendCallback(url: String): String {
		val headers = HttpHeaders().apply {
			contentType = MediaType.APPLICATION_JSON
			set(HttpHeaders.COOKIE, "${WebConstants.TOKEN_COOKIE_NAME}=${fsProperties.token}")
		}

		val entity = HttpEntity<Any>(headers)

		return restTemplate.exchange(url, HttpMethod.GET, entity, String::class.java).body!!
	}
}
