package kim.hyunsub.common.api

import com.fasterxml.jackson.databind.node.ObjectNode
import kim.hyunsub.common.api.model.ApiImageMagickParams
import kim.hyunsub.common.api.model.ApiImageMetadataBulkParams
import kim.hyunsub.common.api.model.ApiImageMetadataResult
import kim.hyunsub.common.api.model.ApiPhotoConvertParams
import kim.hyunsub.common.api.model.UploadResult
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
	fun exif(path: String): String =
		_get("/api/image/exif", mapOf("path" to path))

	fun imageConvert(params: ApiPhotoConvertParams) {
		_post<ObjectNode>("/api/image/convert", params)
	}

	fun imageMagick(params: ApiImageMagickParams) =
		_post<ObjectNode>("/api/image/magick", params)

	fun imageMetadataBulk(params: ApiImageMetadataBulkParams) =
		_post<List<ApiImageMetadataResult>>("/api/image/metadata-bulk", params)

	fun upload(path: String, data: ByteArray, override: Boolean = false) {
		_post<ObjectNode>("/upload/binary", data, mapOf("path" to path, "override" to override.toString()))
	}

	fun uploadByUrl(url: String) =
		_post<UploadResult>("/upload/url", mapOf("url" to url))

	fun get(urlOrPath: String, queryParams: Map<String, String> = emptyMap()): String =
		request(urlOrPath, HttpMethod.GET, queryParams, null)

	fun post(urlOrPath: String, body: Any?): String =
		request(urlOrPath, HttpMethod.POST, emptyMap(), body)

	private inline fun <reified T> _get(urlOrPath: String, queryParams: Map<String, String>) =
		request<T>(urlOrPath, HttpMethod.GET, queryParams, null)

	private inline fun <reified T> _post(urlOrPath: String, body: Any?, queryParams: Map<String, String> = emptyMap()) =
		request<T>(urlOrPath, HttpMethod.POST, queryParams, body)

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
