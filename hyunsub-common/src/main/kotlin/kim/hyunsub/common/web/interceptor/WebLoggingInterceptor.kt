package kim.hyunsub.common.web.interceptor

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.InputStreamReader

class WebLoggingInterceptor : HandlerInterceptor {
	private val log = KotlinLogging.logger { }

	companion object {
		const val ATTR_REQUEST_MILLIS = "ATTR_REQUEST_MILLIS"
	}

	override fun preHandle(req: HttpServletRequest, res: HttpServletResponse, handler: Any): Boolean {
		req.setAttribute(ATTR_REQUEST_MILLIS, System.currentTimeMillis())
		return true
	}

	override fun afterCompletion(
		req: HttpServletRequest,
		res: HttpServletResponse,
		handler: Any,
		ex: Exception?,
	) {
		if (res is ContentCachingResponseWrapper && req is ContentCachingRequestWrapper) {
			logging(req, res)
		}
	}

	private fun logging(
		req: ContentCachingRequestWrapper,
		res: ContentCachingResponseWrapper,
	) {
		if (!log.isDebugEnabled) {
			return
		}

		val ms = (req.getAttribute(ATTR_REQUEST_MILLIS) as? Long)
			?.let { System.currentTimeMillis() - it }
			?: 0

		val method = req.method
		val path = req.requestURI
		val queryString = req.queryString?.let { "?$it" } ?: ""
		val reqBody = contentToString(req.contentAsByteArray)
		val resBody = contentToString(res.contentAsByteArray)
			.let { if (it.length > 1024) "(${it.length} chars)" else it }
		log.debug { "[CALL] $method $path$queryString $reqBody -> (${ms}ms) $resBody" }
	}

	private fun contentToString(content: ByteArray): String =
		ByteArrayInputStream(content)
			.let { InputStreamReader(it) }
			.let { BufferedReader(it) }
			.readLines()
			.joinToString("") { it.trim() }
}
