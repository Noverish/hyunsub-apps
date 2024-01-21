package kim.hyunsub.dutch.bo

import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.dutch.mapper.DutchSettleMapper
import kim.hyunsub.dutch.mapper.DutchTripCurrencyMapper
import kim.hyunsub.dutch.mapper.DutchTripMapper
import kim.hyunsub.dutch.model.dto.DutchSettleParams
import kim.hyunsub.dutch.model.dto.DutchSettleResult
import kim.hyunsub.dutch.model.dto.DutchSettleResultShare
import org.springframework.stereotype.Service
import kotlin.math.roundToInt

@Service
class DutchSettleBo(
	private val dutchSettleMapper: DutchSettleMapper,
	private val dutchTripCurrencyMapper: DutchTripCurrencyMapper,
	private val dutchTripMapper: DutchTripMapper,
) {
	fun settleEach(tripId: String): List<DutchSettleResult> {
		return dutchSettleMapper.settle(tripId)
			.groupBy { it.currency }
			.map { (key, value) ->
				DutchSettleResult(
					currency = key,
					shares = value.map {
						DutchSettleResultShare(
							memberId = it.memberId,
							should = it.should,
							actual = it.actual,
						)
					}
				)
			}
	}

	fun settle(tripId: String, params: DutchSettleParams): Map<String, Int> {
		val trip = dutchTripMapper.select(tripId) ?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such trip")

		val settleResults = dutchSettleMapper.settle(tripId)

		val usedCurrencies = settleResults.map { it.currency }.filter { it != trip.settleCurrency }.toSet()
		val tripCurrencies = dutchTripCurrencyMapper.selectByTripId(tripId)

		val insufficient = usedCurrencies - tripCurrencies.map { it.currency }.toSet()
		if (insufficient.isNotEmpty()) {
			throw ErrorCodeException(ErrorCode.INVALID_PARAMETER, "No currency rate - $insufficient")
		}

		val tripCurrencyMap = tripCurrencies.associate { it.currency to it.rate.toDouble() }
		val result = mutableMapOf<String, Double>()

		settleResults
			.filter { it.memberId != params.mainMemberId }
			.forEach {
				val currency = it.currency
				val amount = it.should - it.actual
				val memberId = it.memberId
				val rate: Double = if (currency == trip.settleCurrency) {
					1.0
				} else {
					tripCurrencyMap[currency]!!
				}

				val value: Double = result[memberId] ?: 0.0
				result[memberId] = value + (rate * amount)
			}

		return result.mapValues { it.value.roundToInt() }
	}
}
