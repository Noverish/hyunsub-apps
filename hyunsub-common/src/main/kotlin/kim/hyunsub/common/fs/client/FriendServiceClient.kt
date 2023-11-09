package kim.hyunsub.common.fs.client

import kim.hyunsub.common.web.config.WebConstants
import kim.hyunsub.friend.model.api.ApiFriendPreview
import kim.hyunsub.friend.model.api.ApiMeetFriendSearchParams
import kim.hyunsub.friend.model.api.ApiMeetFriendSearchResult
import kim.hyunsub.friend.model.dto.MeetFriendBulkUpdateParams
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import java.time.LocalDate

@FeignClient(name = "friendServiceClient", url = "https://friend.hyunsub.kim")
interface FriendServiceClient {
	@GetMapping("/api/v1/meets/{date}/friends")
	fun selectMeetFriends(
		@CookieValue(WebConstants.TOKEN_COOKIE_NAME) token: String,
		@PathVariable date: LocalDate,
	): List<ApiFriendPreview>

	@PutMapping("/api/v1/meets/{date}/friends")
	fun updateMeetFriendsBulk(
		@CookieValue(WebConstants.TOKEN_COOKIE_NAME) token: String,
		@PathVariable date: LocalDate,
		@RequestBody params: MeetFriendBulkUpdateParams,
	): List<ApiFriendPreview>

	@PostMapping("/api/v1/meets/friends/search")
	fun searchMeetFriends(
		@CookieValue(WebConstants.TOKEN_COOKIE_NAME) token: String,
		@RequestBody params: ApiMeetFriendSearchParams,
	): List<ApiMeetFriendSearchResult>
}
