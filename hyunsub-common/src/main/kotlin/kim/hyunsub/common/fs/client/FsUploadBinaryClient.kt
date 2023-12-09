package kim.hyunsub.common.fs.client

import kim.hyunsub.common.fs.config.FsProperties
import kim.hyunsub.common.fs.model.FsSimpleResult
import kim.hyunsub.common.web.config.WebConstants
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import org.springframework.web.util.UriComponentsBuilder

@Service
class FsUploadBinaryClient(
	private val fsProperties: FsProperties,
) {
	private val restTemplate = RestTemplate()

	fun binary(path: String, data: ByteArray, override: Boolean?): FsSimpleResult {
		val url = UriComponentsBuilder.fromHttpUrl(fsProperties.host)
			.path("/upload/binary")
			.queryParam("path", path)
			.queryParam("override", override)
			.toUriString()

		val header = HttpHeaders().apply {
			val cookie = "${WebConstants.TOKEN_COOKIE_NAME}=${fsProperties.token}"
			contentType = MediaType.APPLICATION_JSON
			set(HttpHeaders.COOKIE, cookie)
		}

		val entity = HttpEntity(data, header)

		val res = restTemplate.exchange<FsSimpleResult>(url, HttpMethod.POST, entity)
		return res.body!!
	}
}
