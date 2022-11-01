package kim.hyunsub.division.repository

import kim.hyunsub.division.repository.entity.GatheringUser
import kim.hyunsub.division.repository.entity.GatheringUserId
import org.springframework.data.jpa.repository.JpaRepository

interface GatheringUserRepository: JpaRepository<GatheringUser, GatheringUserId> {
	fun findByGatheringId(gatheringId: String): List<GatheringUser>
	fun findByUserId(userId: String): List<GatheringUser>
}
