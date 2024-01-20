package kim.hyunsub.common.util

import jakarta.servlet.http.HttpServletRequest

fun HttpServletRequest.getCookie(name: String): String? =
	cookies?.firstOrNull { it.name == name }?.value
