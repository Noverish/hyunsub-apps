package kim.hyunsub.common.fs.client

import kim.hyunsub.common.fs.model.UserDeleteParams
import kim.hyunsub.common.fs.model.UserInitParams
import kim.hyunsub.common.model.ApiPageResult
import kim.hyunsub.common.web.config.WebConstants
import kim.hyunsub.common.web.model.SimpleResponse
import kim.hyunsub.photo.model.api.ApiPhotoPreview
import kim.hyunsub.photo.model.dto.PhotoSearchParams
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "photoServiceClient", url = "https://photo.hyunsub.kim")
interface PhotoServiceClient {
	@PostMapping("/api/v1/server/user/init")
	fun userInit(@RequestBody params: UserInitParams): SimpleResponse

	@PostMapping("/api/v1/server/user/delete")
	fun userDelete(@RequestBody params: UserDeleteParams): SimpleResponse

	@PostMapping("/api/v1/search/photos")
	fun searchPhoto(
		@CookieValue(WebConstants.TOKEN_COOKIE_NAME) token: String,
		@RequestBody params: PhotoSearchParams,
	): ApiPageResult<ApiPhotoPreview>
}
