package kim.hyunsub.apparel.model

import java.time.LocalDate

data class RestApiApparel(
	val id: String,
	val itemId: String,
	val name: String,
	val brand: String,
	val size: String,
	val color: String,
	val originPrice: Int,
	val discountPrice: Int?,
	val buyDt: LocalDate,
	val buyLoc: String,
	val makeDt: String?,
	val photos: List<String>,
)
