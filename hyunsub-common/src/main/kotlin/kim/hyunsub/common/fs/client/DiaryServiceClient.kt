package kim.hyunsub.common.fs.client

import kim.hyunsub.common.fs.model.UserDeleteParams
import kim.hyunsub.common.fs.model.UserInitParams
import kim.hyunsub.common.web.model.SimpleResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "diaryServiceClient", url = "https://diary.hyunsub.kim")
interface DiaryServiceClient {
	@PostMapping("/api/v1/server/user/init")
	fun userInit(@RequestBody params: UserInitParams): SimpleResponse

	@PostMapping("/api/v1/server/user/delete")
	fun userDelete(@RequestBody params: UserDeleteParams): SimpleResponse
}
