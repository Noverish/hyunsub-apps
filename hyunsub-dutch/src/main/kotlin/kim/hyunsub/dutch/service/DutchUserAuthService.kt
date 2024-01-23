package kim.hyunsub.dutch.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kim.hyunsub.common.util.decodeBase64
import kim.hyunsub.common.util.getCookie
import kim.hyunsub.common.util.toBase64
import kim.hyunsub.dutch.model.DutchMemberAuth
import org.springframework.stereotype.Service

@Service
class DutchUserAuthService {
	private val mapper = jacksonObjectMapper()

	companion object {
		private const val USER_AUTH_COOKIE = "HYUNSUB_DUTCH"
	}

	fun parse(req: HttpServletRequest, tripId: String): DutchMemberAuth? {
		return parseAll(req)[tripId]
	}

	fun save(req: HttpServletRequest, res: HttpServletResponse, tripId: String, auth: DutchMemberAuth) {
		val map = parseAll(req).toMutableMap()
		map[tripId] = auth
		saveAll(res, map)
	}

	private fun saveAll(res: HttpServletResponse, map: Map<String, DutchMemberAuth>) {
		val json = mapper.writeValueAsString(map)
		val encoded = json.toByteArray().toBase64()
		val cookie = Cookie(USER_AUTH_COOKIE, encoded).apply {
			domain = "hyunsub.kim"
			path = "/"
			maxAge = 365 * 86400
		}
		res.addCookie(cookie)
	}

	private fun parseAll(req: HttpServletRequest): Map<String, DutchMemberAuth> {
		val cookie = req.getCookie(USER_AUTH_COOKIE) ?: return emptyMap()
		val json = String(cookie.decodeBase64())
		return mapper.readValue(json)
	}
}
