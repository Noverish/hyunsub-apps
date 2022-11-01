package kim.hyunsub.division.controller

import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.division.model.GatheringUserCreateParams
import kim.hyunsub.division.model.dto.RestApiGatheringUser
import kim.hyunsub.division.repository.GatheringRepository
import kim.hyunsub.division.repository.GatheringUserRepository
import kim.hyunsub.division.repository.UserRepository
import kim.hyunsub.division.repository.entity.GatheringUser
import kim.hyunsub.division.service.ApiModelConverter
import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/gatherings/{gatheringId}/users")
class GatheringUserController(
	private val gatheringRepository: GatheringRepository,
	private val gatheringUserRepository: GatheringUserRepository,
	private val userRepository: UserRepository,
	private val apiModelConverter: ApiModelConverter,
) {
	val log = KotlinLogging.logger { }

	@GetMapping("")
	fun list(
		userAuth: UserAuth,
		@PathVariable gatheringId: String
	): List<RestApiGatheringUser> {
		log.debug { "[Gathering User List] gatheringId=$gatheringId" }

		val gathering = gatheringRepository.findByIdOrNull(gatheringId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)
		log.debug { "[Gathering User List] gathering=$gathering" }

		val users = gatheringUserRepository.findByGatheringId(gathering.id)
		log.debug { "[Gathering User List] users=$users" }

		if (users.all { it.userId != userAuth.idNo }) {
			throw ErrorCodeException(ErrorCode.NOT_FOUND)
		}
		val userIds = users.map { it.userId }

		return userRepository.findByIdNoIn(userIds)
			.map { apiModelConverter.convert(it) }
	}

	@Authorized(authorities = ["admin"])
	@PostMapping("")
	fun create(
		@PathVariable gatheringId: String,
		@RequestBody params: GatheringUserCreateParams,
	): GatheringUser {
		log.debug { "[Gathering User Create] gatheringId=$gatheringId params=$params" }

		val gathering = gatheringRepository.findByIdOrNull(gatheringId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)
		log.debug { "[Gathering User List] gathering=$gathering" }

		val gatheringUser = GatheringUser(
			gatheringId = gathering.id,
			userId = params.userId,
		)
		log.debug { "[Gathering User List] gatheringUser=$gatheringUser" }

		gatheringUserRepository.save(gatheringUser)

		return gatheringUser
	}
}
