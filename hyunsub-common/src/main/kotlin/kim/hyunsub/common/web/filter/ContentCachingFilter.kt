package kim.hyunsub.common.web.filter

import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 로깅을 위해 요청 응답 데이터를 캐싱
 */
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
class ContentCachingFilter : OncePerRequestFilter() {
	override fun doFilterInternal(
		request: HttpServletRequest,
		response: HttpServletResponse,
		filterChain: FilterChain,
	) {
		val req = ContentCachingRequestWrapper(request)
		val res = ContentCachingResponseWrapper(response)
		filterChain.doFilter(req, res)
		res.copyBodyToResponse()
	}
}
