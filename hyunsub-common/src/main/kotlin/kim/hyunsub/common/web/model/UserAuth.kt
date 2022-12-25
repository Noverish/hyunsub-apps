package kim.hyunsub.common.web.model

import com.fasterxml.jackson.annotation.JsonIgnore

data class UserAuth(
	/** 사용자 ID NO */
	val idNo: String,

	/** 권한 이름 목록 */
	val names: List<String> = emptyList(),

	/** 권한 경로 목록 */
	val paths: List<String> = emptyList(),

	/** 업로드 가능 경로 목록 */
	val uploads: List<String> = emptyList(),

	/** API 목록 */
	val apis: List<String> = emptyList(),
) {
	@get:JsonIgnore
	val isAdmin: Boolean
		get() = names.contains("admin")
}
