package kim.hyunsub.common.api

import kim.hyunsub.common.log.Log
import kim.hyunsub.common.web.config.WebConstants
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.StopWatch
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

class ApiClient(
	private val restTemplate: RestTemplate,
	private val apiProperties: ApiProperties,
) {
	companion object : Log

	inline fun <reified T> typeReference() = object : ParameterizedTypeReference<T>() {}

	inline fun <reified T> get(path: String, queryParams: Map<String, String>): T {
		return request(path, HttpMethod.GET, queryParams, null, typeReference())
	}

	inline fun <reified T> post(path: String, body: Any?): T {
		return request(path, HttpMethod.POST, emptyMap(), body, typeReference())
	}

	fun <T> request(
		path: String,
		method: HttpMethod,
		queryParams: Map<String, String>,
		body: Any?,
		type: ParameterizedTypeReference<T>,
	): T {
		val url = UriComponentsBuilder.newInstance()
			.scheme("https")
			.host(apiProperties.host)
			.path(path)
			.queryParams(LinkedMultiValueMap<String, String>().apply { setAll(queryParams) })
			.toUriString()
			.replace("+", "%2B")

		val headers = HttpHeaders()
		headers.set(HttpHeaders.COOKIE, "${WebConstants.TOKEN_COOKIE_NAME}=${apiProperties.token}")
		headers.contentType = MediaType.APPLICATION_JSON

		val entity = HttpEntity(body, headers)

		val stopWatch = StopWatch()
		stopWatch.start()
		try {
			log.debug("[API Request] {} {} {} {}", method, url, body, headers)
			val res = restTemplate.exchange(url, method, entity, type)
			stopWatch.stop()
			log.debug("[Api Response] {}ms {} {} {}", stopWatch.totalTimeMillis, res.statusCode, res.body, res.headers)
			return res.body!!
		} catch (e: HttpStatusCodeException) {
			stopWatch.stop()
			log.error("[Api Response] {}ms {} {} {}", stopWatch.totalTimeMillis, e.statusCode, e.responseBodyAsString, e.responseHeaders)
			throw e
		}
	}
}
