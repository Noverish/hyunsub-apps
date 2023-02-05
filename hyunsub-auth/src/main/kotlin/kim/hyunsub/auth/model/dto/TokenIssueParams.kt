package kim.hyunsub.auth.model.dto

data class TokenIssueParams(
	val idNo: String,
	val duration: String? = null,
)
