package kim.hyunsub.friend.controller

import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.friend.model.api.ApiFriendPreview
import kim.hyunsub.friend.model.api.toApiPreview
import kim.hyunsub.friend.model.dto.MeetFriendBulkUpdateParams
import kim.hyunsub.friend.repository.FriendMeetRepository
import kim.hyunsub.friend.repository.FriendRepository
import kim.hyunsub.friend.repository.entity.FriendMeet
import kim.hyunsub.friend.repository.entity.FriendMeetId
import mu.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1/meets/{date}/friends")
class MeetFriendController(
	private val friendRepository: FriendRepository,
	private val friendMeetRepository: FriendMeetRepository,
) {
	private val log = KotlinLogging.logger { }

	@GetMapping("")
	fun list(
		userAuth: UserAuth,
		@PathVariable date: LocalDate,
	): List<ApiFriendPreview> {
		val userId = userAuth.idNo
		return friendRepository.selectByMeetDate(userId, date)
			.map { it.toApiPreview() }
	}

	@PutMapping("")
	fun bulkUpdate(
		userAuth: UserAuth,
		@PathVariable date: LocalDate,
		@RequestBody params: MeetFriendBulkUpdateParams,
	): List<ApiFriendPreview> {
		val userId = userAuth.idNo
		val friends = friendRepository.findAllById(params.friendIds)

		val paramFriendIds = friends.map { it.id }
		val dbFriendIds = friendRepository.selectByMeetDate(userId, date).map { it.id }

		val deletes = dbFriendIds - paramFriendIds.toSet()
		val inserts = paramFriendIds - dbFriendIds.toSet()

		log.debug { "[MeetFriendBulkUpdate] deletes=$deletes" }
		log.debug { "[MeetFriendBulkUpdate] inserts=$inserts" }

		friendMeetRepository.deleteAllById(deletes.map { FriendMeetId(userId, it, date) })
		friendMeetRepository.saveAll(inserts.map { FriendMeet(userId, it, date) })

		return friends.map { ApiFriendPreview(it.id, it.name) }
	}
}
