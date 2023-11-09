package kim.hyunsub.friend.repository

import kim.hyunsub.common.util.generateId
import kim.hyunsub.friend.repository.entity.Friend
import kim.hyunsub.friend.repository.entity.FriendMeetAndName
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate

interface FriendRepository : JpaRepository<Friend, String> {
	@Query("SELECT a FROM Friend a WHERE a.fromUserId = :userId ORDER BY a.regDt DESC")
	fun select(userId: String): List<Friend>

	@Query("SELECT a FROM Friend a WHERE a.id = :friendId AND a.fromUserId = :userId")
	fun selectOne(friendId: String, userId: String): Friend?

	@Query("SELECT COUNT(a) FROM Friend a WHERE a.fromUserId = :fromUserId AND a.toUserId = :toUserId")
	fun countByToUserId(fromUserId: String, toUserId: String): Int

	@Query("SELECT COUNT(a) FROM Friend a WHERE a.fromUserId = :userId AND a.name = :name")
	fun countByName(userId: String, name: String): Int

	@Query(
		"""
			SELECT a FROM Friend a
			WHERE a.fromUserId = :userId AND a.name LIKE CONCAT('%', :query, '%')
			ORDER BY a.regDt DESC
		"""
	)
	fun search(userId: String, query: String, page: Pageable): List<Friend>

	@Query(
		"""
			SELECT COUNT(a) FROM Friend a
			WHERE a.fromUserId = :userId AND a.name LIKE CONCAT('%', :query, '%')
			ORDER BY a.regDt DESC
		"""
	)
	fun searchCount(userId: String, query: String): Int

	@Query(
		"""
			SELECT b FROM FriendMeet a
			INNER JOIN Friend b ON b.id = a.friendId
			WHERE a.userId = :userId AND a.date = :date
		"""
	)
	fun selectByMeetDate(userId: String, date: LocalDate): List<Friend>

	@Query(
		"""
			SELECT a.date AS date, b.id AS id, b.name AS name FROM FriendMeet a
			INNER JOIN Friend b ON b.id = a.friendId
			WHERE a.userId = :userId AND a.date IN :dates
		"""
	)
	fun selectByMeetDates(userId: String, dates: List<LocalDate>): List<FriendMeetAndName>
}

fun FriendRepository.generateId() = generateId(16)
