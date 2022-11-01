package kim.hyunsub.division.model

data class ReportResult(
	val currency: CurrencyCode,
	val data: Map<String, Int>,
)
