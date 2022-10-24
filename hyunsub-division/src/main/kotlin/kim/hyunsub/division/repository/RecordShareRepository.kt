package kim.hyunsub.division.repository

import kim.hyunsub.division.repository.entity.RecordShare
import org.springframework.data.jpa.repository.JpaRepository

interface RecordShareRepository: JpaRepository<RecordShare, String>
