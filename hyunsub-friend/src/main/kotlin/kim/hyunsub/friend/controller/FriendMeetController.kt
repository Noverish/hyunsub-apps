package kim.hyunsub.friend.controller

import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.friend.model.api.ApiFriendMeet
import kim.hyunsub.friend.model.api.toApi
import kim.hyunsub.friend.model.dto.FriendMeetCreateParams
import kim.hyunsub.friend.repository.FriendMeetRepository
import kim.hyunsub.friend.repository.entity.FriendMeet
import kim.hyunsub.friend.repository.entity.FriendMeetId
import kim.hyunsub.friend.repository.findByIdOrNull
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1/friends/{friendId}/meets")
class FriendMeetController(
	val friendMeetRepository: FriendMeetRepository,
) {
	@GetMapping("")
	fun list(
		userAuth: UserAuth,
		@PathVariable friendId: String,
	): List<ApiFriendMeet> {
		return friendMeetRepository.findByFriendId(friendId).map { it.toApi() }
	}

	@PostMapping("")
	fun create(
		userAuth: UserAuth,
		@PathVariable friendId: String,
		@RequestBody params: FriendMeetCreateParams,
	): ApiFriendMeet {
		val exist = friendMeetRepository.findByIdOrNull(friendId, params.date)
		if (exist != null) {
			throw ErrorCodeException(ErrorCode.ALREADY_EXIST)
		}

		val friendMeet = FriendMeet(
			friendId = friendId,
			date = params.date,
		)
		friendMeetRepository.save(friendMeet)
		return friendMeet.toApi()
	}

	@GetMapping("/{date}")
	fun get(
		userAuth: UserAuth,
		@PathVariable friendId: String,
		@PathVariable date: LocalDate,
	): ApiFriendMeet {
		val friendMeet = friendMeetRepository.findByIdOrNull(friendId, date)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)
		return friendMeet.toApi()
	}

	@DeleteMapping("/{date}")
	fun delete(
		userAuth: UserAuth,
		@PathVariable friendId: String,
		@PathVariable date: LocalDate,
	): ApiFriendMeet {
		val friendMeet = friendMeetRepository.findByIdOrNull(friendId, date)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)
		friendMeetRepository.deleteById(FriendMeetId(friendId, date))
		return friendMeet.toApi()
	}
}
