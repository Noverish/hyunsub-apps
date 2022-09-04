package kim.hyunsub.common.api

import com.fasterxml.jackson.databind.node.ObjectNode
import kim.hyunsub.common.api.model.FileStat
import kim.hyunsub.common.api.model.VideoThumbnailParams
import kim.hyunsub.common.api.model.VideoThumbnailResult

class ApiCaller(
	private val apiClient: ApiClient,
) {
	fun walkDetail(path: String): List<FileStat> =
		apiClient.get("/api//fs/walk/detail", mapOf("path" to path))

	fun readdirDetail(path: String): List<FileStat> =
		apiClient.get("/api/fs/readdir/detail", mapOf("path" to path))

	fun ffprobe(path: String): ObjectNode =
		apiClient.get("/api/video/ffprobe", mapOf("path" to path))

	fun videoThumbnail(params: VideoThumbnailParams): VideoThumbnailResult =
		apiClient.post("/api/video/generate-thumbnail", params)
}
