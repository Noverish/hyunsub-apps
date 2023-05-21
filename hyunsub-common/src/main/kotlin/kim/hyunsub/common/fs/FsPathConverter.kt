package kim.hyunsub.common.fs

import org.springframework.web.util.UriComponentsBuilder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import kotlin.io.path.Path

object FsPathConverter {
	private const val host = "https://file.hyunsub.kim"
	private const val nonceBase: String = "/hyunsub/file/upload"

	fun noncePath(nonce: String): String =
		Path(nonceBase, nonce).toString()

	fun convertToUrl(path: String): String {
		val encodedPath = URLEncoder.encode(path, StandardCharsets.UTF_8)
			.replace("%2F", "/")
			.replace("+", "%20")

		return UriComponentsBuilder.fromHttpUrl(host)
			.path(encodedPath)
			.build(false)
			.toUriString()
	}

	fun thumbnailUrl(path: String?): String =
		path?.let { convertToUrl(it) } ?: "/img/placeholder.jpg"
}
