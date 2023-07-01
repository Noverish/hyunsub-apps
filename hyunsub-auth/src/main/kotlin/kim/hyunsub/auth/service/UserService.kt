package kim.hyunsub.auth.service

import at.favre.lib.crypto.bcrypt.BCrypt
import kim.hyunsub.auth.config.AuthConstants
import kim.hyunsub.auth.model.UserInfo
import kim.hyunsub.auth.model.dto.UserCreateParams
import kim.hyunsub.auth.repository.UserAuthorityRepository
import kim.hyunsub.auth.repository.UserRepository
import kim.hyunsub.auth.repository.entity.User
import kim.hyunsub.auth.repository.entity.UserAuthority
import kim.hyunsub.auth.repository.generateId
import kim.hyunsub.common.fs.FsClient
import kim.hyunsub.common.fs.model.FsRsyncParams
import kim.hyunsub.common.fs.remove
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserService(
	private val fsClient: FsClient,
	private val userRepository: UserRepository,
	private val userAuthorityRepository: UserAuthorityRepository,
) {
	fun list(): List<UserInfo> {
		val users = userRepository.findAll()
		val userAuthorities = userAuthorityRepository.findAll()

		val authorityMap = userAuthorities.groupBy { it.userIdNo }

		return users.map {
			UserInfo(it, authorityMap[it.idNo] ?: emptyList())
		}
	}

	fun create(params: UserCreateParams): UserInfo {
		val hashedPassword = BCrypt.withDefaults().hashToString(AuthConstants.BCRYPT_COST, "password".toCharArray())

		val user = User(
			idNo = userRepository.generateId(),
			username = params.name,
			password = hashedPassword,
		)

		fsClient.rsync(
			FsRsyncParams(
				from = "/hyunsub/drive/base",
				to = "/hyunsub/drive/${user.idNo}",
			)
		)

		userRepository.save(user)

		val authorities = listOf(1000, 2000, 3000, 4000).map { UserAuthority(user.idNo, it) }
		userAuthorityRepository.saveAll(authorities)

		return UserInfo(user, authorities)
	}

	fun get(idNo: String): UserInfo {
		val user = userRepository.findByIdOrNull(idNo)
			?: throw ErrorCodeException(ErrorCode.NOT_EXIST_USER)

		val authorities = userAuthorityRepository.findByUserIdNo(idNo)
		return UserInfo(user, authorities)
	}

	fun delete(idNo: String): UserInfo {
		val info = get(idNo)

		fsClient.remove("/hyunsub/drive/${info.user.idNo}")

		userAuthorityRepository.deleteAll(info.authorities)
		userRepository.delete(info.user)

		return info
	}
}
