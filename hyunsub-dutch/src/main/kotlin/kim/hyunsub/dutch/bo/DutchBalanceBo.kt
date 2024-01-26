package kim.hyunsub.dutch.bo

import kim.hyunsub.dutch.mapper.DutchBalanceMapper
import kim.hyunsub.dutch.mapper.DutchSpendMapper
import kim.hyunsub.dutch.model.api.ApiDutchBalance
import kim.hyunsub.dutch.model.dto.DutchBalanceCreateParams
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

	fun create(memberId: String, params: DutchBalanceCreateParams): ApiDutchBalance {
		val balance = DutchBalance(
			memberId = memberId,
			currency = params.currency,
			amount = params.amount,
		)

		dutchBalanceMapper.insert(balance)

		return list(memberId).first { it.currency == params.currency }
	}
}
