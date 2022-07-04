package kim.hyunsub.common.web.model

data class UserAuth(
	/** 사용자 ID NO */
	val idNo: String,

	/** 권한이 있는 경로 모음 */
	val paths: List<String> = emptyList(),
)
