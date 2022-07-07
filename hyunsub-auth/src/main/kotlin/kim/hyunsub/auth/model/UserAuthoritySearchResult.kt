package kim.hyunsub.auth.model

data class UserAuthoritySearchResult(
	/** 권한 이름 목록 */
	val names: List<String> = emptyList(),

	/** 권한 경로 목록 */
	val paths: List<String> = emptyList(),
)
