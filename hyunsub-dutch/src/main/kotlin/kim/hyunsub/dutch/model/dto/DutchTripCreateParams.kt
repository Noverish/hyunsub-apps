package kim.hyunsub.dutch.model.dto

data class DutchTripCreateParams(
	val name: String,
	val members: List<String>,
)
