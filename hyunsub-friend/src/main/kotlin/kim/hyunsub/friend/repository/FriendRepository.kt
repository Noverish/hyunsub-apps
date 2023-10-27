package kim.hyunsub.friend.repository

import kim.hyunsub.common.util.generateId
import kim.hyunsub.friend.repository.entity.Friend
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface FriendRepository : JpaRepository<Friend, String> {
	@Query("SELECT a FROM Friend a WHERE a.fromUserId = :userId ORDER BY a.regDt DESC")
	fun select(userId: String): List<Friend>

	@Query("SELECT COUNT(a) FROM Friend a WHERE a.fromUserId = :fromUserId AND a.toUserId = :toUserId")
	fun checkExist(fromUserId: String, toUserId: String): Int

	@Query("SELECT COUNT(a) FROM Friend a WHERE a.fromUserId = :userId AND a.name = :name")
	fun checkExistName(userId: String, name: String): Int
}

fun FriendRepository.generateId() = generateId(16)
