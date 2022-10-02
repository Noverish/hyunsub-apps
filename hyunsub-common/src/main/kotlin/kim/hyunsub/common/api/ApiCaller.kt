package kim.hyunsub.common.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import kim.hyunsub.common.api.model.*
import kim.hyunsub.common.http.HttpClient
import kim.hyunsub.common.web.config.WebConstants
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.util.UriComponentsBuilder

class ApiCaller(
	private val httpClient: HttpClient,
	private val apiProperties: ApiProperties,
) {
	fun stat(path: String): FileStat? {
		return try {
			_get("/api/fs/stat", mapOf("path" to path))
		} catch (ex: HttpClientErrorException) {
			null
		}
	}

	fun mkdir(path: String) {
		_post<ObjectNode>("/api/fs/mkdir", mapOf("path" to path))
	}

	fun rename(from: String, to: String) {
		_post<ObjectNode>("/api/fs/rename", mapOf("from" to from, "to" to to))
	}

	fun unlink(path: String) {
		_post<ObjectNode>("/api/fs/unlink", mapOf("path" to path))
	}

	fun walk(path: String): List<String> =
		_get("/api/fs/walk", mapOf("path" to path))

	fun walkDetail(path: String): List<FileStat> =
		_get("/api/fs/walk/detail", mapOf("path" to path))

	fun readdirDetail(path: String): List<FileStat> =
		_get("/api/fs/readdir/detail", mapOf("path" to path))

	fun copyMDate(from: String, to: String) {
		_post<ObjectNode>("/api/fs/copy-mdate", mapOf("from" to from, "to" to to))
	}

	fun ffprobe(path: String): ObjectNode =
		_get("/api/video/ffprobe", mapOf("path" to path))

	fun videoThumbnail(params: VideoThumbnailParams): VideoThumbnailResult =
		_post("/api/video/generate-thumbnail", params)

	fun exif(path: String): String =
		_get("/api/image/exif", mapOf("path" to path))

	fun imageConvert(params: PhotoConvertParams) {
		_post<ObjectNode>("/api/image/convert", params)
	}

	fun upload(path: String, data: ByteArray, override: Boolean = false) {
		_post<ObjectNode>("/upload/binary", data, mapOf("path" to path, "override" to override.toString()))
	}

	fun uploadByUrl(url: String) =
		_post<UploadResult>("/upload/url", mapOf("url" to url))

	fun ffmpeg(params: FFmpegParams): FFmpegResult =
		_post("/api/video/ffmpeg", params)

	fun ffmpegStatus(): FFmpegStatus =
		_get("/api/video/ffmpeg/status", emptyMap())

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
