package kim.hyunsub.common.api

import com.fasterxml.jackson.databind.node.ObjectNode
import kim.hyunsub.common.api.model.FileStat
import kim.hyunsub.common.api.model.VideoThumbnailParams
import kim.hyunsub.common.api.model.VideoThumbnailResult
import org.springframework.web.client.HttpClientErrorException

class ApiCaller(
	private val apiClient: ApiClient,
) {
	fun stat(path: String): FileStat? {
		return try {
			apiClient.get("/api/fs/stat", mapOf("path" to path))
		} catch (ex: HttpClientErrorException) {
			null
		}
	}

	fun mkdir(path: String) {
		apiClient.post<ObjectNode>("/api/fs/mkdir", mapOf("path" to path))
	}

	fun rename(from: String, to: String) {
		apiClient.post<ObjectNode>("/api/fs/rename", mapOf("from" to from, "to" to to))
	}

	fun walkDetail(path: String): List<FileStat> =
		apiClient.get("/api/fs/walk/detail", mapOf("path" to path))

	fun readdirDetail(path: String): List<FileStat> =
		apiClient.get("/api/fs/readdir/detail", mapOf("path" to path))

	fun ffprobe(path: String): ObjectNode =
		apiClient.get("/api/video/ffprobe", mapOf("path" to path))

	fun videoThumbnail(params: VideoThumbnailParams): VideoThumbnailResult =
		apiClient.post("/api/video/generate-thumbnail", params)
}
