package kim.hyunsub.common.fs

import com.fasterxml.jackson.databind.node.ObjectNode
import kim.hyunsub.common.api.model.ApiSimpleResult
import kim.hyunsub.common.api.model.VideoThumbnailParams
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "fsVideoClient", url = "\${fs.host}")
interface FsVideoClient {
	@GetMapping("/api/video/ffprobe")
	fun ffprobe(@RequestParam path: String): ObjectNode

	@PostMapping("/api/video/generate-thumbnail")
	fun videoThumbnail(@RequestBody params: VideoThumbnailParams): ApiSimpleResult
}
