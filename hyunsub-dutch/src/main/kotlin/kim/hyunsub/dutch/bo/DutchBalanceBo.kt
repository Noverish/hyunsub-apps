package kim.hyunsub.dutch.bo

import kim.hyunsub.dutch.mapper.DutchBalanceMapper
import kim.hyunsub.dutch.mapper.DutchSpendMapper
import kim.hyunsub.dutch.model.api.ApiDutchBalance
import kim.hyunsub.dutch.model.dto.DutchBalanceUpdateBulkParams
import kim.hyunsub.dutch.model.dto.DutchSpendSearchQuery
import kim.hyunsub.dutch.repository.entity.DutchBalance
import org.springframework.stereotype.Service

@Service
class DutchBalanceBo(
	private val dutchBalanceMapper: DutchBalanceMapper,
	private val dutchSpendMapper: DutchSpendMapper,
) {
	fun list(memberId: String): List<ApiDutchBalance> {
		val query = DutchSpendSearchQuery(memberId = memberId)
		val spendMap = dutchSpendMapper.search(query).groupBy { it.currency }
		val balanceMap = dutchBalanceMapper.selectByMemberId(memberId).associateBy { it.currency }

		return spendMap.map { (currency, list) ->
			val balance = balanceMap[currency]
			val spends = list.sumOf { it.actual }

			ApiDutchBalance(
				currency = currency,
				amount = balance?.amount,
				spends = spends
			)
		}
	}

	fun updateBulk(memberId: String, params: DutchBalanceUpdateBulkParams): List<ApiDutchBalance> {
		val balanceMap = dutchBalanceMapper.selectByMemberId(memberId).associateBy { it.currency }

		for (item in params.data) {
			val exist = balanceMap[item.currency]
			if (exist != null) {
				val newBalance = exist.copy(amount = item.amount)
				dutchBalanceMapper.update(newBalance)
			} else {
				val balance = DutchBalance(
					memberId = memberId,
					currency = item.currency,
					amount = item.amount,
				)
				dutchBalanceMapper.insert(balance)
			}
		}

		return list(memberId)
	}
}
