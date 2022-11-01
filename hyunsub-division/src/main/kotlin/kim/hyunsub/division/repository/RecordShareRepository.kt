package kim.hyunsub.division.repository

import kim.hyunsub.division.repository.entity.RecordShare
import org.springframework.data.jpa.repository.JpaRepository

interface RecordShareRepository: JpaRepository<RecordShare, String> {
	fun findByRecordId(recordId: String): List<RecordShare>
	fun findByRecordIdIn(recordIds: List<String>): List<RecordShare>
	fun findByUserId(userId: String): List<RecordShare>
}
