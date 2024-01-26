package kim.hyunsub.dutch.mapper

import kim.hyunsub.dutch.model.dto.DutchSpendSearchQuery
import kim.hyunsub.dutch.repository.entity.DutchSpend
import org.apache.ibatis.annotations.Mapper

@Mapper
interface DutchSpendMapper {
	fun count(query: DutchSpendSearchQuery): Int
	fun search(query: DutchSpendSearchQuery): List<DutchSpend>
}
