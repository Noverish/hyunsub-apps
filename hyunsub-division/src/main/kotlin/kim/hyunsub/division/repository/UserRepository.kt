package kim.hyunsub.division.repository

import kim.hyunsub.division.repository.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, String> {
	fun findByUsername(username: String): User?
	fun findByIdNoIn(idNos: List<String>): List<User>
}
