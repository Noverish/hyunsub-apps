package kim.hyunsub.common.http

import kim.hyunsub.common.log.Log
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.StopWatch
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

class HttpClient(private val restTemplate: RestTemplate) {
	companion object : Log

	inline fun <reified T> typeReference() = object : ParameterizedTypeReference<T>() {}

	inline fun <reified T> get(
		url: String,
		queryParams: Map<String, String> = emptyMap(),
		headers: Map<String, String> = emptyMap(),
	): T {
		return request(url, HttpMethod.GET, queryParams, headers, null, typeReference())
	}

	inline fun <reified T> post(
		url: String,
		body: Any? = null,
		queryParams: Map<String, String> = emptyMap(),
		headers: Map<String, String> = emptyMap(),
	): T {
		return request(url, HttpMethod.POST, queryParams, headers, body, typeReference())
	}

	fun <T> request(
		url: String,
		method: HttpMethod,
		queryParams: Map<String, String> = emptyMap(),
		headers: Map<String, String> = emptyMap(),
		body: Any? = null,
		type: ParameterizedTypeReference<T>,
	): T {
		val newUrl = UriComponentsBuilder.fromHttpUrl(url)
			.queryParams(LinkedMultiValueMap<String, String>().apply { setAll(queryParams) })
			.toUriString()
			.replace("+", "%2B")

		val httpHeaders = HttpHeaders().apply { setAll(headers) }
		val entity = HttpEntity(body, httpHeaders)

		val stopWatch = StopWatch()
		stopWatch.start()
		try {
			if (body is ByteArray) {
				log.debug("[API Request] {} {} bytes={} {}", method, newUrl, body.size, headers)
			} else {
				log.debug("[API Request] {} {} {} {}", method, newUrl, body, headers)
			}
			val res = restTemplate.exchange(newUrl, method, entity, type)
			stopWatch.stop()
			log.debug("[Api Response] {}ms {} {} {}", stopWatch.totalTimeMillis, res.statusCode, res.body, res.headers)
			return res.body!!
		} catch (e: HttpStatusCodeException) {
			stopWatch.stop()
			if (body is ByteArray) {
				log.error("[API Request] {} {} bytes={} {}", method, newUrl, body.size, headers)
			} else {
				log.error("[API Request] {} {} {} {}", method, newUrl, body, headers)
			}
			log.error("[Api Response] {}ms {} {} {}", stopWatch.totalTimeMillis, e.statusCode, e.responseBodyAsString, e.responseHeaders)
			throw e
		}
	}
}
