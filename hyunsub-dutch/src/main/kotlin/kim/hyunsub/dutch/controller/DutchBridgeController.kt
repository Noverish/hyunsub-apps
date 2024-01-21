package kim.hyunsub.dutch.controller

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kim.hyunsub.dutch.bo.DutchBridgeBo
import kim.hyunsub.dutch.config.DutchIgnoreAuthorize
import kim.hyunsub.dutch.model.DutchMemberAuth
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class DutchBridgeController(
	private val dutchBridgeBo: DutchBridgeBo,
) {
	@DutchIgnoreAuthorize
	@RequestMapping("/bridge/entry/{tripId}")
	fun entry(
		dutchMemberAuth: DutchMemberAuth?,
		@PathVariable tripId: String,
	): String {
		val redirect = dutchBridgeBo.entry(dutchMemberAuth, tripId)
		return "redirect:$redirect"
	}

	@DutchIgnoreAuthorize
	@RequestMapping("/bridge/entry/{tripId}/member")
	fun memberSelect(
		req: HttpServletRequest,
		res: HttpServletResponse,
		@PathVariable tripId: String,
		@RequestParam memberId: String,
	): String {
		val redirect = dutchBridgeBo.memberSelect(req, res, tripId, memberId)
		return "redirect:$redirect"
	}
}
