package kim.hyunsub.dutch.repository

import kim.hyunsub.common.util.generateId
import kim.hyunsub.dutch.repository.entity.DutchMember
import org.springframework.data.jpa.repository.JpaRepository

interface DutchMemberRepository : JpaRepository<DutchMember, String>

fun DutchMemberRepository.generateId() = generateId(8)
