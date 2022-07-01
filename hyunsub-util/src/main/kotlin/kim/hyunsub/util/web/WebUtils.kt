package kim.hyunsub.util.web

import javax.servlet.http.HttpServletRequest

fun HttpServletRequest.isLocalhost() =
	("127.0.0.1" == remoteAddr) || ("::1" == remoteAddr) || ("0:0:0:0:0:0:0:1" == remoteAddr)
