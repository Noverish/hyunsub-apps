package kim.hyunsub.photo.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("photo")
data class PhotoProperties(
	val host: String,
)
