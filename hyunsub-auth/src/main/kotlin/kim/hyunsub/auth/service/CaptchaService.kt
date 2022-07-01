package kim.hyunsub.auth.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kim.hyunsub.auth.config.CaptchaProperties
import kim.hyunsub.auth.model.captcha.CaptchaVerifyParams
import kim.hyunsub.auth.model.captcha.CaptchaVerifyResult
import kim.hyunsub.util.log.Log
import org.springframework.http.*
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange


@Service
class CaptchaService(private val captchaProperties: CaptchaProperties) {
	companion object : Log

	private val restTemplate = RestTemplate()
	private val mapper = jacksonObjectMapper()
	private val url = "https://www.google.com/recaptcha/api/siteverify"

	init {
		MappingJackson2HttpMessageConverter().apply {
			supportedMediaTypes = listOf(MediaType.APPLICATION_FORM_URLENCODED)
		}.let {
			restTemplate.messageConverters.add(it)
		}
	}

	fun verify(captcha: String, remoteAddr: String?): Boolean {
		val params = CaptchaVerifyParams(
			secret = captchaProperties.secretKey,
			response = captcha,
			remoteip = remoteAddr,
		)

		// TODO 이렇게 MultiValueMap 으로 안하고 자동으로 application/x-www-form-urlencoded 형태로 바꿔주는 방법 찾기
		val map = LinkedMultiValueMap<String, String>()
		map["secret"] = listOf(params.secret)
		map["response"] = listOf(params.response)
		map["remoteip"] = listOf(params.remoteip)

		val headers = HttpHeaders()
		headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

		// TODO HttpClient 만들기
		val res = restTemplate.exchange<String>(url, HttpMethod.POST, HttpEntity(map, headers)).body!!
		log.debug("Verify Captcha: params={}, result={}", params, res)
		val result = mapper.readValue<CaptchaVerifyResult>(res)
		return result.success
	}
}
