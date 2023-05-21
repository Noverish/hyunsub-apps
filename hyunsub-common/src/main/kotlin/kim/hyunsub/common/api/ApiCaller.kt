package kim.hyunsub.common.api

import kim.hyunsub.common.http.HttpClient
import kim.hyunsub.common.web.config.WebConstants
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.web.util.UriComponentsBuilder

class ApiCaller(
	private val httpClient: HttpClient,
	private val apiProperties: ApiProperties,
) {
	fun get(urlOrPath: String, queryParams: Map<String, String> = emptyMap()): String =
		request(urlOrPath, HttpMethod.GET, queryParams, null)

	fun post(urlOrPath: String, body: Any?): String =
		request(urlOrPath, HttpMethod.POST, emptyMap(), body)

	private inline fun <reified T> request(
		urlOrPath: String,
		method: HttpMethod,
		queryParams: Map<String, String>,
		body: Any?,
	): T {
		val url = if (urlOrPath.startsWith("https://")) {
			urlOrPath
		} else {
			UriComponentsBuilder.newInstance()
				.scheme("https")
				.host(apiProperties.host)
				.path(urlOrPath)
				.build(false)
				.toUriString()
		}

		val cookie = "${WebConstants.TOKEN_COOKIE_NAME}=${apiProperties.token}"
		val headers = buildMap {
			this += (HttpHeaders.CONTENT_TYPE to MediaType.APPLICATION_JSON_VALUE)
			this += (HttpHeaders.COOKIE to cookie)
		}

		return httpClient.request(
			url = url,
			method = method,
			queryParams = queryParams,
			headers = headers,
			body = body,
			type = httpClient.typeReference(),
		)
	}
}
