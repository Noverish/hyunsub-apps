package kim.hyunsub.encode.repository

import kim.hyunsub.encode.repository.entity.Encode
import org.springframework.data.jpa.repository.JpaRepository

interface EncodeRepository: JpaRepository<Encode, Int> {
	fun findByEndDtIsNull(): List<Encode>
}
