package kim.hyunsub.division.repository.entity

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.Table

@Entity
@Table(name = "division_gathering_user")
@IdClass(GatheringUserId::class)
data class GatheringUser(
	@Id
	val gatheringId: String,

	@Id
	val userId: String,
)
