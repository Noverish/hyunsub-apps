package kim.hyunsub.division.repository.entity

import java.io.Serializable
import javax.persistence.Embeddable

@Embeddable
data class GatheringUserId(
	var gatheringId: String,
	var userId: String,
) : Serializable
