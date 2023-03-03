package kim.hyunsub.encode.service

import com.fasterxml.jackson.databind.JsonNode
import kim.hyunsub.common.api.ApiCaller
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class VideoMetadataCacheService(
	private val apiCaller: ApiCaller,
) {
	val log = KotlinLogging.logger { }
	val map = ConcurrentHashMap<String, JsonNode>()

	fun getOrFetch(path: String): JsonNode {
		return map[path] ?: run {
			log.info { "[VideoMetadata] fetch $path" }
			val result = apiCaller.ffprobe(path)
			map[path] = result
			return result
		}
	}
}
