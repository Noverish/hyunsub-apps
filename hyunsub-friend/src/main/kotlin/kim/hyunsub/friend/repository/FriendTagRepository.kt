package kim.hyunsub.friend.repository

import kim.hyunsub.friend.repository.entity.FriendTag
import kim.hyunsub.friend.repository.entity.FriendTagId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface FriendTagRepository : JpaRepository<FriendTag, FriendTagId> {
	fun findByFriendId(friendId: String): List<FriendTag>

	@Query(
		"""
			SELECT DISTINCT(a.tag)
			FROM FriendTag a
			INNER JOIN Friend b ON b.id = a.friendId
			WHERE b.fromUserId = :userId
		"""
	)
	fun selectDistinctTag(userId: String): List<String>

	@Modifying
	fun deleteByFriendId(friendId: String): Int
}
