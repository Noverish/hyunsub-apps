package kim.hyunsub.apparel.model.dto

import kim.hyunsub.apparel.model.ApiApparelInfo

data class ApparelUpdateParams(
	val info: ApiApparelInfo,
	val uploads: List<String>,
	val deletes: List<String>,
)
