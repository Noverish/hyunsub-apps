package kim.hyunsub.auth.repository

import kim.hyunsub.auth.repository.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, String> {
	fun findByUsername(username: String): User?
}
