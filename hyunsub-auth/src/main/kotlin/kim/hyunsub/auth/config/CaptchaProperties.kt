package kim.hyunsub.auth.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("captcha")
data class CaptchaProperties(
	val secretKey: String,
)
