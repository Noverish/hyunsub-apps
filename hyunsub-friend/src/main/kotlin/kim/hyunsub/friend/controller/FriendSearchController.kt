package kim.hyunsub.friend.controller

import kim.hyunsub.common.model.RestApiPageResult
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.friend.model.api.ApiFriendPreview
import kim.hyunsub.friend.model.api.toApiPreview
import kim.hyunsub.friend.model.dto.FriendSearchParams
import kim.hyunsub.friend.repository.FriendRepository
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/search")
class FriendSearchController(
	private val friendRepository: FriendRepository,
) {
	@PostMapping("")
	fun search(
		userAuth: UserAuth,
		@RequestBody params: FriendSearchParams,
	): RestApiPageResult<ApiFriendPreview> {
		val query = params.query ?: ""
		val userId = userAuth.idNo
		val pageRequest = PageRequest.of(params.page, params.pageSize)
		val total = friendRepository.searchCount(userId, query)
		val result = friendRepository.search(userId, query, pageRequest)
		return RestApiPageResult(
			total = total,
			page = params.page,
			pageSize = params.pageSize,
			data = result.map { it.toApiPreview() },
		)
	}
}
