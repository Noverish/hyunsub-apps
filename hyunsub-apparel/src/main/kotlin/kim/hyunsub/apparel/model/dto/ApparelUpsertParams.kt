package kim.hyunsub.apparel.model.dto

import kim.hyunsub.apparel.model.RestApiApparel

data class ApparelUpsertParams(
	val apparel: RestApiApparel,
	val deletes: List<String> = emptyList(),
	val uploads: List<ApparelUpsertImageParams> = emptyList(),
)

data class ApparelUpsertImageParams(
	val nonce: String,
	val ext: String,
)
