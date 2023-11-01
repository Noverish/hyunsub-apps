package kim.hyunsub.friend.repository

import kim.hyunsub.friend.model.api.ApiFriendTagPreview
import kim.hyunsub.friend.repository.entity.Friend
import kim.hyunsub.friend.repository.entity.FriendTag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface FriendTagRepository : JpaRepository<FriendTag, String> {
	@Query("SELECT a.tag FROM FriendTag a WHERE a.userId = :userId AND a.friendId = :friendId")
	fun selectTag(userId: String, friendId: String): List<String>

	@Modifying
	@Query("DELETE FROM FriendTag a WHERE a.userId = :userId AND a.friendId = :friendId")
	fun delete(userId: String, friendId: String): Int

	@Query(
		"""
			SELECT new kim.hyunsub.friend.model.api.ApiFriendTagPreview(a.tag, COUNT(a.tag))
			FROM FriendTag a
			WHERE a.userId = :userId
			GROUP BY a.tag
		"""
	)
	fun selectDistinctTag(userId: String): List<ApiFriendTagPreview>

	@Query(
		"""
			SELECT b
			FROM FriendTag a
			INNER JOIN Friend b ON b.id = a.friendId
			WHERE a.userId = :userId AND a.tag = :tag
		"""
	)
	fun selectFriendByTag(userId: String, tag: String): List<Friend>
}
