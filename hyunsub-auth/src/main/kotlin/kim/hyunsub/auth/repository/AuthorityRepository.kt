package kim.hyunsub.auth.repository

import kim.hyunsub.auth.repository.entity.Authority
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface AuthorityRepository : JpaRepository<Authority, Int> {
	@Query("SELECT a FROM Authority a WHERE a.default = TRUE")
	fun findDefaults(): List<Authority>
}
