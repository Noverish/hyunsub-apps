package kim.hyunsub.common.fs

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("fs")
data class FsProperties(
	val host: String,
	val token: String,
	val nonceBase: String = "/hyunsub/file/upload",
)
