package kim.hyunsub.friend.repository

import kim.hyunsub.common.util.generateId
import kim.hyunsub.friend.repository.entity.Friend
import org.springframework.data.jpa.repository.JpaRepository

interface FriendRepository : JpaRepository<Friend, String>

fun FriendRepository.generateId() = generateId(16)
