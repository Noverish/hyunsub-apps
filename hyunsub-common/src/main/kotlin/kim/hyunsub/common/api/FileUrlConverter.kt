package kim.hyunsub.common.api

import org.springframework.web.util.UriComponentsBuilder

class FileUrlConverter(
	private val apiProperties: ApiProperties,
) {
	fun pathToUrl(path: String): String {
		return UriComponentsBuilder.newInstance()
			.scheme("https")
			.host(apiProperties.host) // TODO 이걸 Constant 에서 관리하게 하기
			.path(path)
			.toUriString()
	}
}
