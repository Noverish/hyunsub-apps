package kim.hyunsub.encode.repository

import kim.hyunsub.encode.repository.entity.Encode
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface EncodeRepository : JpaRepository<Encode, Int> {
	@Query(
		"""
		SELECT e FROM Encode e
		WHERE e.startDt IS NOT NULL AND e.endDt IS NULL
	"""
	)
	fun getNowEncoding(): Encode?

	@Query(
		"""
		SELECT e FROM Encode e
		WHERE e.startDt IS NULL AND e.endDt IS NULL
		ORDER BY e.regDt
	"""
	)
	fun getCandidates(): List<Encode>
}
