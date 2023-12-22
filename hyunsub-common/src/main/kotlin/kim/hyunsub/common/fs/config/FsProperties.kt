package kim.hyunsub.common.fs.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("fs")
data class FsProperties(
	val host: String,
	val token: String,
	val nonceBase: String = "/hyunsub/file/upload",
)
