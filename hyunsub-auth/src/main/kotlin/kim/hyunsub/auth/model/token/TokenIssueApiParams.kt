package kim.hyunsub.auth.model.token

data class TokenIssueApiParams(
	val idNo: String,
	val duration: String? = null,
)
