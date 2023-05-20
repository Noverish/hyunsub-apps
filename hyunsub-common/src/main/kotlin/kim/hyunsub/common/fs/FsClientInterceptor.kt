package kim.hyunsub.common.fs

import feign.RequestInterceptor
import feign.RequestTemplate
import kim.hyunsub.common.web.config.WebConstants
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component

@Component
class FsClientInterceptor(
	private val fsProperties: FsProperties,
) : RequestInterceptor {
	override fun apply(template: RequestTemplate) {
		val cookie = "${WebConstants.TOKEN_COOKIE_NAME}=${fsProperties.token}"
		template.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
		template.header(HttpHeaders.COOKIE, cookie)
	}
}
