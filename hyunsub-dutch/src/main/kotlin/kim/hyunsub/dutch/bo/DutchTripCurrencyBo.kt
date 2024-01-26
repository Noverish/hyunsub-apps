package kim.hyunsub.dutch.bo

import kim.hyunsub.dutch.mapper.DutchRecordMapper
import kim.hyunsub.dutch.mapper.DutchTripCurrencyMapper
import kim.hyunsub.dutch.model.DutchCurrency
import kim.hyunsub.dutch.model.api.ApiDutchTripCurrency
import kim.hyunsub.dutch.model.dto.DutchTripCurrencyUpdateParams
import kim.hyunsub.dutch.repository.entity.DutchTripCurrency
import kim.hyunsub.dutch.service.DutchTripDao
import org.springframework.stereotype.Service

@Service
class DutchTripCurrencyBo(
	private val dutchTripDao: DutchTripDao,
	private val dutchTripCurrencyMapper: DutchTripCurrencyMapper,
	private val dutchRecordMapper: DutchRecordMapper,
) {
	fun list(tripId: String): List<ApiDutchTripCurrency> {
		val trip = dutchTripDao.selectOrThrow(tripId)

		val tripCurrencyMap = dutchTripCurrencyMapper.selectByTripId(tripId)
			.associateBy { it.currency }

		val currencies = dutchRecordMapper.selectCurrency(tripId)
			.filter { it != trip.settleCurrency }

		return currencies.map {
			ApiDutchTripCurrency(
				currency = it,
				rate = tripCurrencyMap[it]?.rate?.toDouble()
			)
		}
	}

	fun set(tripId: String, currency: DutchCurrency, params: DutchTripCurrencyUpdateParams): ApiDutchTripCurrency {
		val entity = DutchTripCurrency(
			tripId = tripId,
			currency = currency,
			rate = params.rate.toString(),
		)

		dutchTripCurrencyMapper.upsert(entity)

		return ApiDutchTripCurrency(
			currency = currency,
			rate = params.rate,
		)
	}
}
