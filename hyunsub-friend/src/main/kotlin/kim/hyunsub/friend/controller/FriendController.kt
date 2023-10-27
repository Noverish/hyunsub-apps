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
import kim.hyunsub.friend.repository.FriendRepository
import kim.hyunsub.friend.repository.FriendTagRepository
import kim.hyunsub.friend.repository.entity.Friend
import kim.hyunsub.friend.repository.entity.FriendTag
import kim.hyunsub.friend.repository.generateId
import org.springframework.data.repository.findByIdOrNull
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
		params.userId?.let {
			val exist = friendRepository.checkExist(userAuth.idNo, it)
			if (exist > 0) {
				throw ErrorCodeException(ErrorCode.ALREADY_EXIST)
			}
		}

		if (friendRepository.checkExistName(userAuth.idNo, params.name) > 0) {
			throw ErrorCodeException(ErrorCode.ALREADY_EXIST, "already exist name: ${params.name}")
		}

		val friend = Friend(
			id = friendRepository.generateId(),
			fromUserId = userAuth.idNo,
			toUserId = params.userId,
			name = params.name,
			birthday = params.birthday,
			description = params.description,
		)

		val tags = params.tags.map { FriendTag(friend.id, it) }

		friendRepository.save(friend)
		friendTagRepository.saveAll(tags)

		return friend.toApi(params.tags)
	}

	@GetMapping("/{friendId}")
	fun get(
		userAuth: UserAuth,
		@PathVariable friendId: String,
	): ApiFriend {
		val friend = friendRepository.findByIdOrNull(friendId)
			?.takeIf { it.fromUserId == userAuth.idNo }
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)
		val tags = friendTagRepository.findByFriendId(friendId).map { it.tag }
		return friend.toApi(tags)
	}

	@PutMapping("/{friendId}")
	fun update(
		userAuth: UserAuth,
		@PathVariable friendId: String,
		@RequestBody params: FriendUpdateParams,
	): ApiFriend {
		val friend = friendRepository.findByIdOrNull(friendId)
			?.takeIf { it.fromUserId == userAuth.idNo }
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)
		friend.name = params.name
		friend.birthday = params.birthday
		friend.description = params.description
		friendRepository.save(friend)

		val tags = params.tags.map { FriendTag(friend.id, it) }
		friendTagRepository.deleteByFriendId(friendId)
		friendTagRepository.saveAll(tags)

		return friend.toApi(params.tags)
	}

	@DeleteMapping("/{friendId}")
	fun delete(
		userAuth: UserAuth,
		@PathVariable friendId: String,
	): ApiFriend {
		val friend = friendRepository.findByIdOrNull(friendId)
			?.takeIf { it.fromUserId == userAuth.idNo }
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)
		val tags = friendTagRepository.findByFriendId(friendId).map { it.tag }

		friendRepository.deleteById(friendId)
		friendTagRepository.deleteByFriendId(friendId)

		return friend.toApi(tags)
	}
}
