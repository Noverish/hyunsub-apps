package kim.hyunsub.dutch.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.Table
import java.io.Serializable
import java.time.LocalDateTime

@Entity
@Table(name = "dutch_record_member")
@IdClass(DutchRecordMemberId::class)
data class DutchRecordMember(
	@Id
	val recordId: String,

	@Id
	val memberId: String,

	@Column(nullable = false)
	val actual: Int,

	@Column(nullable = false)
	val should: Int,

	@Column(nullable = false)
	val regDt: LocalDateTime = LocalDateTime.now().withNano(0),
)

data class DutchRecordMemberId(
	val recordId: String = "",
	val memberId: String = "",
) : Serializable
