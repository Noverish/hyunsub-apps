package kim.hyunsub.common.api

import kim.hyunsub.common.api.model.FileStat
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.stereotype.Service

class ApiCaller(
	private val apiClient: ApiClient,
) {
	fun walk2(path: String): List<FileStat> =
		apiClient.get("/api/walk2", mapOf("path" to path))

	fun readdirDetail(path: String): List<FileStat> =
		apiClient.get("/api/fs/readdir/detail", mapOf("path" to path))
}
