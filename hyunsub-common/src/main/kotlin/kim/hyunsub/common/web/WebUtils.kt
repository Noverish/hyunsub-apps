package kim.hyunsub.common.web

import javax.servlet.http.HttpServletRequest

val HttpServletRequest.originalIp: String
	get() = this.getHeader("X-Original-IP")
