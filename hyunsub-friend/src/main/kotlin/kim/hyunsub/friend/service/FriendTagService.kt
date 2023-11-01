package kim.hyunsub.friend.service

import kim.hyunsub.friend.repository.FriendTagRepository
import kim.hyunsub.friend.repository.entity.Friend
import kim.hyunsub.friend.repository.entity.FriendTag
import org.springframework.stereotype.Service

@Service
class FriendTagService(
	private val friendTagRepository: FriendTagRepository,
) {
	fun update(friend: Friend, newTags: List<String>) {
		val userId = friend.fromUserId
		val friendId = friend.id

		val oldTags = friendTagRepository.selectTag(userId, friendId)

		val inserts = newTags - oldTags.toSet()
		val deletes = oldTags - newTags.toSet()

		friendTagRepository.saveAll(
			inserts.map {
				FriendTag(userId, friendId, it)
			}
		)

		friendTagRepository.deleteAll(
			deletes.map {
				FriendTag(userId, friendId, it)
			}
		)
	}
}
