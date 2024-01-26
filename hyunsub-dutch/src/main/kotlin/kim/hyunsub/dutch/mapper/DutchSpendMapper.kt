package kim.hyunsub.dutch.mapper

import kim.hyunsub.dutch.repository.entity.DutchSpend
import org.apache.ibatis.annotations.Mapper
import org.springframework.data.domain.Pageable

@Mapper
interface DutchSpendMapper {
	fun count(tripId: String, memberId: String): Int
	fun select(tripId: String, memberId: String, page: Pageable): List<DutchSpend>
}
