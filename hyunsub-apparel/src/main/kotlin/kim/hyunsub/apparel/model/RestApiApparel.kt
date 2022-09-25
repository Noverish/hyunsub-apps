package kim.hyunsub.apparel.model

import java.time.LocalDate

data class RestApiApparel(
	val id: String,
	val itemNo: String,
	val name: String,
	val brand: String,
	val type: String,
	val size: String,
	val color: String,
	val originPrice: Int,
	val discountPrice: Int?,
	val material: String,
	val size2: String,
	val buyDt: LocalDate,
	val buyLoc: String,
	val makeDt: String?,
	val images: List<RestApiApparelImage>,
)
