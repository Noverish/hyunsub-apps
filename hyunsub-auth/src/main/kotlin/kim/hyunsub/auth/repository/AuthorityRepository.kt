package kim.hyunsub.auth.repository

import kim.hyunsub.auth.repository.entity.Authority
import org.springframework.data.jpa.repository.JpaRepository

interface AuthorityRepository : JpaRepository<Authority, Int>
