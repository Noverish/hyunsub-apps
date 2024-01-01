package kim.hyunsub.dutch.model.api

data class ApiDutchRecordDetail(
	val record: ApiDutchRecordPreview,
	val members: List<ApiDutchRecordMember>,
)
