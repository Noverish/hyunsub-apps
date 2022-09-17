package kim.hyunsub.common.api

import org.springframework.web.util.UriComponentsBuilder
import kotlin.io.path.Path

class FileUrlConverter(
	private val apiProperties: ApiProperties,
) {
	fun pathToUrl(path: String): String =
		UriComponentsBuilder.newInstance()
			.scheme("https")
			.host(apiProperties.host)
			.path(path)
			.toUriString()

	fun getNoncePath(nonce: String): String =
		Path(apiProperties.nonceBase, nonce).toString()
}
