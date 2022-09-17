package kim.hyunsub.photo.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("photo")
data class PhotoProperties(
	val host: String,
)
