package kim.hyunsub.auth.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kim.hyunsub.auth.config.CaptchaProperties
import kim.hyunsub.auth.model.captcha.CaptchaVerifyParams
import kim.hyunsub.auth.model.captcha.CaptchaVerifyResult
import kim.hyunsub.common.http.HttpClient
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap

@Service
class CaptchaService(
	private val captchaProperties: CaptchaProperties,
	private val httpClient: HttpClient,
) {
	private val mapper = jacksonObjectMapper()
	private val mapType = mapper.typeFactory.constructMapType(Map::class.java, String::class.java, String::class.java)
	private val url = "https://www.google.com/recaptcha/api/siteverify"

	fun verify(captcha: String, remoteAddr: String?): Boolean {
		val params = CaptchaVerifyParams(
			secret = captchaProperties.secretKey,
			response = captcha,
			remoteip = remoteAddr,
		)

		val data = LinkedMultiValueMap<String, String>().apply {
			setAll(mapper.convertValue(params, mapType))
		}

		val headers = mapOf(HttpHeaders.CONTENT_TYPE to MediaType.APPLICATION_FORM_URLENCODED_VALUE)

		val res: String = httpClient.post(url, data, headers)
		val result = mapper.readValue<CaptchaVerifyResult>(res)
		return result.success
	}
}
