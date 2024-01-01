package kim.hyunsub.dutch.model.api

import kim.hyunsub.dutch.repository.entity.DutchRecordMemberWithName

data class ApiDutchRecordMember(
	val recordId: String,
	val memberId: String,
	val actual: Double,
	val should: Double,
	val name: String,
)

fun DutchRecordMemberWithName.toApi() = ApiDutchRecordMember(
	recordId = recordId,
	memberId = memberId,
	actual = actual,
	should = should,
	name = name,
)
