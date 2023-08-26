package kim.hyunsub.auth.model

data class TokenPayload(
	val idNo: String,
	val username: String,
	val authorities: List<String>,
	val lang: String?,
)
