package kim.hyunsub.common.web

import jakarta.servlet.http.HttpServletRequest

val HttpServletRequest.originalIp: String
	get() = this.getHeader("X-Original-IP")
