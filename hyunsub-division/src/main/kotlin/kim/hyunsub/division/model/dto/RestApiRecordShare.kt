package kim.hyunsub.division.model.dto

data class RestApiRecordShare(
	val id: String,
	val userId: String,
	val realAmount: Int,
	val shouldAmount: Int,
)
