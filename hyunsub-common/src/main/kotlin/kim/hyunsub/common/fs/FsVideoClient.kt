package kim.hyunsub.common.fs

import com.fasterxml.jackson.databind.node.ObjectNode
import kim.hyunsub.common.fs.model.FFmpegParams
import kim.hyunsub.common.fs.model.FFmpegResult
import kim.hyunsub.common.fs.model.VideoThumbnailParams
import kim.hyunsub.common.fs.model.VideoThumbnailResult
import kim.hyunsub.common.fs.model.YoutubeDownloadParams
import kim.hyunsub.common.fs.model.YoutubeDownloadResult
import kim.hyunsub.common.fs.model.YoutubeMetadata
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "fsVideoClient", url = "\${fs.host}")
interface FsVideoClient {
	@GetMapping("/api/video/ffprobe")
	fun ffprobe(@RequestParam path: String): ObjectNode

	@PostMapping("/api/video/ffmpeg")
	fun ffmpeg(@RequestBody params: FFmpegParams): FFmpegResult

	@PostMapping("/api/video/generate-thumbnail")
	fun videoThumbnail(@RequestBody params: VideoThumbnailParams): VideoThumbnailResult

	@GetMapping("/api/video/youtube/metadata")
	fun youtubeMetadata(@RequestParam url: String): YoutubeMetadata

	@PostMapping("/api/video/youtube/download")
	fun youtubeDownload(@RequestBody params: YoutubeDownloadParams): YoutubeDownloadResult
}
