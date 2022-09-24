package kim.hyunsub.video.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("video")
data class VideoProperties(
	val host: String,
)
