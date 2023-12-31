package kim.hyunsub.dutch.repository

import kim.hyunsub.dutch.repository.entity.DutchRecordMember
import kim.hyunsub.dutch.repository.entity.DutchRecordMemberId
import org.springframework.data.jpa.repository.JpaRepository

interface DutchRecordMemberRepository : JpaRepository<DutchRecordMember, DutchRecordMemberId>
