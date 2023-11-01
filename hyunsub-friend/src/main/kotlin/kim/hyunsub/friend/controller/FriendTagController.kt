package kim.hyunsub.friend.controller

import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.friend.model.api.ApiFriendTagDetail
import kim.hyunsub.friend.model.api.ApiFriendTagPreview
import kim.hyunsub.friend.model.api.toApiPreview
import kim.hyunsub.friend.repository.FriendTagRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
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
	): List<ApiFriendTagPreview> {
		return friendTagRepository.selectDistinctTag(userAuth.idNo)
	}

	@GetMapping("/{tag}")
	fun detail(
		@PathVariable tag: String,
		userAuth: UserAuth,
	): ApiFriendTagDetail {
		val userId = userAuth.idNo

		val friends = friendTagRepository.selectFriendByTag(userId, tag)
			.map { it.toApiPreview() }

		return ApiFriendTagDetail(
			name = tag,
			count = friends.size.toLong(),
			friends = friends
		)
	}
}
