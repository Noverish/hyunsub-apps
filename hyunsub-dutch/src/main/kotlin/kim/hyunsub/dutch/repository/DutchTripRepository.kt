package kim.hyunsub.dutch.repository

import kim.hyunsub.common.util.generateId
import kim.hyunsub.dutch.repository.entity.DutchTrip
import org.springframework.data.jpa.repository.JpaRepository

interface DutchTripRepository : JpaRepository<DutchTrip, String>

fun DutchTripRepository.generateId() = generateId(6)
