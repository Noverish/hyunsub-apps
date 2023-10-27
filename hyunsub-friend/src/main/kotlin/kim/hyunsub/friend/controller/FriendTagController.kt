package kim.hyunsub.friend.controller

import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.friend.repository.FriendTagRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/tags")
class FriendTagController(
	val friendTagRepository: FriendTagRepository,
) {
	@GetMapping("")
	fun list(
		userAuth: UserAuth,
	): List<String> {
		return friendTagRepository.selectDistinctTag(userAuth.idNo)
	}
}
