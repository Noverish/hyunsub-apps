package kim.hyunsub.common.api

import org.springframework.web.util.UriComponentsBuilder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import kotlin.io.path.Path

object FileUrlConverter {
	fun getNoncePath(nonce: String): String =
		Path(ApiConstants.nonceBase, nonce).toString()

	fun convertToUrl(path: String): String {
		val encodedPath = URLEncoder.encode(path, StandardCharsets.UTF_8)
			.replace("%2F", "/")
			.replace("+", "%20")

		return UriComponentsBuilder.fromHttpUrl(ApiConstants.host)
			.path(encodedPath)
			.build(false)
			.toUriString()
	}

	fun thumbnailUrl(path: String?): String =
		path?.let { convertToUrl(it) } ?: "/img/placeholder.jpg"
}
