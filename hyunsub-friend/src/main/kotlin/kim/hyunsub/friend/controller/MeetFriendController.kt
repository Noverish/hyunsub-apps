package kim.hyunsub.friend.controller

import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.friend.model.api.ApiMeetFriend
import kim.hyunsub.friend.repository.FriendMeetRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1/meets/{date}/friends")
class MeetFriendController(
	val friendMeetRepository: FriendMeetRepository,
) {
	@GetMapping("")
	fun list(
		userAuth: UserAuth,
		@PathVariable date: LocalDate,
	): List<ApiMeetFriend> {
		return friendMeetRepository.findByDate(date)
	}
}
