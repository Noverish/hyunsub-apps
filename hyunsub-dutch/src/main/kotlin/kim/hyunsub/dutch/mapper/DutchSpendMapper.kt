package kim.hyunsub.dutch.mapper

import kim.hyunsub.dutch.repository.entity.DutchSpend
import kim.hyunsub.dutch.repository.entity.DutchSpendSum
import org.apache.ibatis.annotations.Mapper
import org.springframework.data.domain.Pageable

@Mapper
interface DutchSpendMapper {
	fun count(tripId: String, memberId: String): Int
	fun select(tripId: String, memberId: String, page: Pageable): List<DutchSpend>
	fun sum(tripId: String, memberId: String): List<DutchSpendSum>
}
