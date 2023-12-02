package kim.hyunsub.apparel.model

data class ApiApparel(
	val id: String,
	val info: ApiApparelInfo,
	val images: List<ApiApparelImage>,
)
