package kim.hyunsub.dutch.repository

import kim.hyunsub.common.util.generateId
import kim.hyunsub.dutch.repository.entity.DutchRecord
import org.springframework.data.jpa.repository.JpaRepository

interface DutchRecordRepository : JpaRepository<DutchRecord, String>

fun DutchRecordRepository.generateId() = generateId(10)
