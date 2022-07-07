package kim.hyunsub.common.web.model

data class UserAuth(
	/** 사용자 ID NO */
	val idNo: String,

	/** 권한 이름 목록 */
	val authorityNames: List<String> = emptyList(),

	/** 권한 경로 목록 */
	val authorityPaths: List<String> = emptyList(),
)
