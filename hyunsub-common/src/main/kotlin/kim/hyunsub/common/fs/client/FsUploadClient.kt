package kim.hyunsub.common.fs.client

import kim.hyunsub.common.fs.model.UploadUrlParams
import kim.hyunsub.common.fs.model.UploadUrlResult
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "fsUploadClient", url = "\${fs.host}")
interface FsUploadClient {
	@PostMapping("/upload/url")
	fun url(@RequestBody params: UploadUrlParams): UploadUrlResult
}

fun FsUploadClient.url(url: String) =
	url(UploadUrlParams(url))
