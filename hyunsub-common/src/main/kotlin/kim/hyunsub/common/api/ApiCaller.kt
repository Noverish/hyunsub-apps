package kim.hyunsub.common.api

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
			get("/api/fs/stat", mapOf("path" to path))
		} catch (ex: HttpClientErrorException) {
			null
		}
	}

	fun mkdir(path: String) {
		post<ObjectNode>("/api/fs/mkdir", mapOf("path" to path))
	}

	fun rename(from: String, to: String) {
		post<ObjectNode>("/api/fs/rename", mapOf("from" to from, "to" to to))
	}

	fun walk(path: String): List<String> =
		get("/api/fs/walk", mapOf("path" to path))

	fun walkDetail(path: String): List<FileStat> =
		get("/api/fs/walk/detail", mapOf("path" to path))

	fun readdirDetail(path: String): List<FileStat> =
		get("/api/fs/readdir/detail", mapOf("path" to path))

	fun ffprobe(path: String): ObjectNode =
		get("/api/video/ffprobe", mapOf("path" to path))

	fun videoThumbnail(params: VideoThumbnailParams): VideoThumbnailResult =
		post("/api/video/generate-thumbnail", params)

	fun exif(path: String): String =
		get("/api/image/exif", mapOf("path" to path))

	fun imageConvert(params: PhotoConvertParams) {
		post<ObjectNode>("/api/image/convert", params)
	}

	fun upload(path: String, data: ByteArray, override: Boolean = false) {
		post<ObjectNode>("/upload/binary", data, mapOf("path" to path, "override" to override.toString()))
	}

	fun ffmpeg(params: FFmpegParams): FFmpegResult =
		post("/api/video/ffmpeg", params)

	fun ffmpegStatus(): FFmpegStatus =
		get("/api/video/ffmpeg/status", emptyMap())

	private inline fun <reified T> get(path: String, queryParams: Map<String, String>) =
		request<T>(path, HttpMethod.GET, queryParams, null)

	private inline fun <reified T> post(path: String, body: Any?, queryParams: Map<String, String> = emptyMap()) =
		request<T>(path, HttpMethod.POST, queryParams, body)

	private inline fun <reified T> request(
		path: String,
		method: HttpMethod,
		queryParams: Map<String, String>,
		body: Any?,
	): T {
		val url = UriComponentsBuilder.newInstance()
			.scheme("https")
			.host(apiProperties.host)
			.path(path)
			.toUriString()

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
