package kim.hyunsub.division.repository

import kim.hyunsub.division.repository.entity.Record
import org.springframework.data.jpa.repository.JpaRepository

interface RecordRepository: JpaRepository<Record, String>
