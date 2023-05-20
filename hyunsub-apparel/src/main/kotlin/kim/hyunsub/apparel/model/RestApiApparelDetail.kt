package kim.hyunsub.apparel.model

data class RestApiApparelDetail(
	val apparel: RestApiApparel,
	val images: List<RestApiApparelImage>,
)
