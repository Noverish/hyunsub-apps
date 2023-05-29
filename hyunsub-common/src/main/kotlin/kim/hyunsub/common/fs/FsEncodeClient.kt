package kim.hyunsub.common.fs

import com.fasterxml.jackson.databind.JsonNode
import kim.hyunsub.common.fs.model.EncodeParams
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "fsEncodeClient", url = "https://encode.hyunsub.kim")
interface FsEncodeClient {
	@PostMapping("/api/v1/encode")
	fun encode(@RequestBody params: EncodeParams): JsonNode
}
