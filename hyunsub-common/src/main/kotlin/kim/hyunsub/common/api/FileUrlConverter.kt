package kim.hyunsub.common.api

import org.springframework.web.util.UriComponentsBuilder

class FileUrlConverter(
	private val apiProperties: ApiProperties,
) {
	fun pathToUrl(path: String): String {
		return UriComponentsBuilder.newInstance()
			.scheme("https")
			.host(apiProperties.host)
			.path(path)
			.toUriString()
	}
}
