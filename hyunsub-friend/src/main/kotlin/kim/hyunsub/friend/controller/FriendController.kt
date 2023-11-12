package kim.hyunsub.friend.controller

import kim.hyunsub.common.annotation.HyunsubCors
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.friend.model.api.ApiFriend
import kim.hyunsub.friend.model.api.ApiFriendPreview
import kim.hyunsub.friend.model.api.toApi
import kim.hyunsub.friend.model.api.toApiPreview
import kim.hyunsub.friend.model.dto.FriendCreateParams
import kim.hyunsub.friend.model.dto.FriendUpdateParams
import kim.hyunsub.friend.repository.FriendMeetRepository
import kim.hyunsub.friend.repository.FriendRepository
import kim.hyunsub.friend.repository.FriendTagRepository
import kim.hyunsub.friend.repository.entity.Friend
import kim.hyunsub.friend.repository.generateId
import kim.hyunsub.friend.service.FriendTagService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/friends")
class FriendController(
	private val friendRepository: FriendRepository,
	private val friendTagRepository: FriendTagRepository,
	private val friendTagService: FriendTagService,
	private val friendMeetRepository: FriendMeetRepository,
) {
	@HyunsubCors
	@GetMapping("")
	fun list(
		userAuth: UserAuth,
	): List<ApiFriendPreview> {
		return friendRepository.select(userAuth.idNo).map { it.toApiPreview() }
	}

	@PostMapping("")
	fun create(
		userAuth: UserAuth,
		@RequestBody params: FriendCreateParams,
	): ApiFriend {
		val userId = userAuth.idNo

		params.userId?.let {
			if (friendRepository.countByToUserId(userId, it) > 0) {
				throw ErrorCodeException(ErrorCode.ALREADY_EXIST, "already exist user: ${params.userId}")
			}
		}

		if (friendRepository.countByName(userId, params.name) > 0) {
			throw ErrorCodeException(ErrorCode.ALREADY_EXIST, "already exist name: ${params.name}")
		}

		val friend = Friend(
			id = friendRepository.generateId(),
			fromUserId = userId,
			toUserId = params.userId,
			name = params.name,
			birthday = params.birthday,
			description = params.description,
		)
		friendRepository.save(friend)

		friendTagService.update(friend, params.tags)

		return friend.toApi(params.tags, emptyList())
	}

	@GetMapping("/{friendId}")
	fun detail(
		userAuth: UserAuth,
		@PathVariable friendId: String,
	): ApiFriend? {
		val userId = userAuth.idNo
		val friend = friendRepository.selectOne(friendId, userId)
			?: return null

		val tags = friendTagRepository.selectTag(userId, friendId)

		val meets = friendMeetRepository.select(userId, friendId).map { it.date }

		return friend.toApi(tags, meets)
	}

	@PutMapping("/{friendId}")
	fun update(
		userAuth: UserAuth,
		@PathVariable friendId: String,
		@RequestBody params: FriendUpdateParams,
	): ApiFriend {
		val userId = userAuth.idNo
		val friend = friendRepository.selectOne(friendId, userId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		friend.name = params.name
		friend.birthday = params.birthday
		friend.description = params.description
		friendRepository.save(friend)

		friendTagService.update(friend, params.tags)

		val meets = friendMeetRepository.select(userId, friendId).map { it.date }

		return friend.toApi(params.tags, meets)
	}

	@DeleteMapping("/{friendId}")
	fun delete(
		userAuth: UserAuth,
		@PathVariable friendId: String,
	): ApiFriend {
		val userId = userAuth.idNo
		val friend = friendRepository.selectOne(friendId, userId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val tags = friendTagRepository.selectTag(userId, friendId)

		val meets = friendMeetRepository.select(userId, friendId).map { it.date }

		friendRepository.deleteById(friendId)
		friendTagRepository.delete(userId, friendId)
		friendMeetRepository.delete(userId, friendId)

		return friend.toApi(tags, meets)
	}
}
