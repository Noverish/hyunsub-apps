package kim.hyunsub.friend.controller

import kim.hyunsub.common.model.ApiPageResult
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.friend.model.api.ApiFriendPreview
import kim.hyunsub.friend.model.api.ApiFriendTagPreview
import kim.hyunsub.friend.model.api.toApiPreview
import kim.hyunsub.friend.repository.FriendTagRepository
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
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

	@GetMapping("/{tag}/friends")
	fun friendsOfTag(
		userAuth: UserAuth,
		@PathVariable tag: String,
		@RequestParam p: Int?,
	): ApiPageResult<ApiFriendPreview> {
		val page = p ?: 0
		val pageSize = 10
		val userId = userAuth.idNo
		val total = friendTagRepository.friendsOfTagCount(userId, tag)
		val pageRequest = PageRequest.of(page, pageSize)
		val result = friendTagRepository.friendsOfTag(userId, tag, pageRequest)
		return ApiPageResult(
			total = total,
			page = page,
			pageSize = pageSize,
			data = result.map { it.toApiPreview() }
		)
	}
}
