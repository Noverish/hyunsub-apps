package kim.hyunsub.auth.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("captcha")
data class CaptchaProperties(
	val secretKey: String
)
