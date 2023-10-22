package kim.hyunsub.common.fs.client

import kim.hyunsub.common.web.config.WebConstants
import kim.hyunsub.friend.model.api.ApiMeetFriend
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import java.time.LocalDate

@FeignClient(name = "friendServiceClient", url = "https://friend.hyunsub.kim")
interface FriendServiceClient {
	@GetMapping("/api/v1/meets/{date}/friends")
	fun selectMeetFriends(
		@CookieValue(WebConstants.TOKEN_COOKIE_NAME) token: String,
		@PathVariable date: LocalDate,
	): List<ApiMeetFriend>
}
