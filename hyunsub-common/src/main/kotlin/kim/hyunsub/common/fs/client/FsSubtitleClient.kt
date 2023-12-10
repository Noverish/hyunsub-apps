package kim.hyunsub.common.fs.client

import kim.hyunsub.common.fs.model.FsSimpleResult
import kim.hyunsub.common.fs.model.SubtitleSyncParams
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "fsSubtitleClient", url = "\${fs.host}")
interface FsSubtitleClient {
	@PostMapping("/api/subtitle/sync")
	fun sync(@RequestBody params: SubtitleSyncParams): FsSimpleResult
}
