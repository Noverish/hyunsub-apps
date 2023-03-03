package kim.hyunsub.common.http

import mu.KotlinLogging
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
	private val log = KotlinLogging.logger { }

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
			.build(false)
			.toUriString()
			.replace("+", "%2B")

		val httpHeaders = HttpHeaders().apply { setAll(headers) }
		val entity = HttpEntity(body, httpHeaders)

		val stopWatch = StopWatch()
		stopWatch.start()
		try {
			val res = restTemplate.exchange(newUrl, method, entity, type)
			stopWatch.stop()
			log.debug { "[HTTP] $method $newUrl -> ${res.statusCode.value()} ${stopWatch.totalTimeMillis}ms" }
			return res.body!!
		} catch (e: HttpStatusCodeException) {
			stopWatch.stop()
			log.error { "[HTTP] $method $newUrl -> ${e.statusCode.value()} ${stopWatch.totalTimeMillis}ms ${e.responseBodyAsString}" }
			throw e
		}
	}
}
