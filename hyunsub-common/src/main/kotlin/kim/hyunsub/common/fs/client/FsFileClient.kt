package kim.hyunsub.common.fs.client

import kim.hyunsub.common.fs.config.FsProperties
import kim.hyunsub.common.web.config.WebConstants
import mu.KotlinLogging
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import org.springframework.web.util.UriComponentsBuilder

@Component
class FsFileClient(
	private val fsProperties: FsProperties,
) {
	private val template = RestTemplate()
	private val log = KotlinLogging.logger { }

	private fun generateHeader() = HttpHeaders().apply {
		val cookie = "${WebConstants.TOKEN_COOKIE_NAME}=${fsProperties.token}"
		set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
		set(HttpHeaders.COOKIE, cookie)
	}

	private fun generateUrl(path: String) = UriComponentsBuilder
		.fromHttpUrl(fsProperties.host)
		.path(path)
		.build(false)
		.toUriString()

	fun readAsString(path: String): String {
		val url = generateUrl(path)
		val method = HttpMethod.GET

		val start = System.currentTimeMillis()
		try {
			val res = template.exchange<String>(url, method, HttpEntity<Nothing>(generateHeader()))
			val elapsedTime = System.currentTimeMillis() - start
			val status = res.statusCode
			log.debug { "[FS] $method $url --> ${elapsedTime}ms $status" }
			return res.body!!
		} catch (ex: HttpStatusCodeException) {
			val elapsedTime = System.currentTimeMillis() - start
			val status = ex.statusCode
			log.debug { "[FS] $method $url --> ${elapsedTime}ms $status" }
			throw ex
		}
	}
}
