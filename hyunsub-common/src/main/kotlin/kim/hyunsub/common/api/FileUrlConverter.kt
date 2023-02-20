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

	companion object {
		fun convertToUrl(path: String): String =
			UriComponentsBuilder.newInstance()
				.scheme("https")
				.host(ApiConstants.host)
				.path(path)
				.toUriString()

		fun thumbnailUrl(path: String?): String =
			path?.let { convertToUrl(it) } ?: "/img/placeholder.jpg"
	}
}
