package kim.hyunsub.auth.repository

import kim.hyunsub.auth.repository.entity.UserAuthority
import kim.hyunsub.auth.repository.entity.UserAuthorityId
import org.springframework.data.jpa.repository.JpaRepository

interface UserAuthorityRepository : JpaRepository<UserAuthority, UserAuthorityId> {
	fun findByUserIdNo(userIdNo: String): List<UserAuthority>
}
