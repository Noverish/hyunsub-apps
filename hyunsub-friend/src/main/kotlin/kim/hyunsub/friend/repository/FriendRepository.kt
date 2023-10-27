package kim.hyunsub.friend.repository

import kim.hyunsub.common.util.generateId
import kim.hyunsub.friend.repository.entity.Friend
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface FriendRepository : JpaRepository<Friend, String> {
	@Query("SELECT a FROM Friend a ORDER BY a.regDt DESC")
	fun select(): List<Friend>

	@Query("SELECT COUNT(a) FROM Friend a WHERE a.fromUserId = :fromUserId AND a.toUserId = :toUserId")
	fun checkExist(fromUserId: String, toUserId: String): Int
}

fun FriendRepository.generateId() = generateId(16)
