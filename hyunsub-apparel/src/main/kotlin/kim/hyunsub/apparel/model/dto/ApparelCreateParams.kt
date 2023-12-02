package kim.hyunsub.apparel.model.dto

import kim.hyunsub.apparel.model.ApiApparelInfo

data class ApparelCreateParams(
	val info: ApiApparelInfo,
	val uploads: List<String>,
)
