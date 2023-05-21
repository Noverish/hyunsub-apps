package kim.hyunsub.common.fs

import kim.hyunsub.common.fs.model.FsSimpleResult
import kim.hyunsub.common.fs.model.ImageConvertParams
import kim.hyunsub.common.fs.model.ImageMagickParams
import kim.hyunsub.common.fs.model.ImageMetadataBulkParams
import kim.hyunsub.common.fs.model.ImageMetadataResult
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "fsImageClient", url = "\${fs.host}")
interface FsImageClient {
	@GetMapping("/api/image/exif")
	fun exif(@RequestParam path: String): String

	@PostMapping("/api/image/convert")
	fun convert(@RequestBody params: ImageConvertParams): FsSimpleResult

	@PostMapping("/api/image/magick")
	fun magick(@RequestBody params: ImageMagickParams): FsSimpleResult

	@PostMapping("/api/image/metadata-bulk")
	fun metadataBulk(@RequestBody params: ImageMetadataBulkParams): List<ImageMetadataResult>
}
