package kim.hyunsub.diary.model.dto

data class DiaryUpdateParams(
	val content: String,
	val summary: String,
	val friendIds: List<String>,
)
