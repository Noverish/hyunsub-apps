package kim.hyunsub.auth.service

import kim.hyunsub.common.web.model.UserAuth
import org.springframework.stereotype.Service

@Service
class TokenGenerator(
	private val authorityService: AuthorityService,
	private val jwtService: JwtService,
) {
	fun generateToken(idNo: String): String {
		val searchResult = authorityService.searchAuthorities(idNo)

		val payload = UserAuth(
			idNo = idNo,
			authorityNames = searchResult.names,
			authorityPaths = searchResult.paths,
		)

		return jwtService.issue(payload)
	}
}
