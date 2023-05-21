package kim.hyunsub.common.fs

import kim.hyunsub.common.fs.model.FsSimpleResult
import kim.hyunsub.common.fs.model.UploadUrlParams
import kim.hyunsub.common.fs.model.UploadUrlResult
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "fsUploadClient", url = "\${fs.host}")
interface FsUploadClient {
	@PostMapping("/upload/binary")
	fun binary(
		@RequestParam path: String,
		@RequestBody data: ByteArray,
		@RequestParam override: Boolean?,
	): FsSimpleResult

	@PostMapping("/upload/url")
	fun url(@RequestBody params: UploadUrlParams): UploadUrlResult
}

fun FsUploadClient.url(url: String) =
	url(UploadUrlParams(url))
