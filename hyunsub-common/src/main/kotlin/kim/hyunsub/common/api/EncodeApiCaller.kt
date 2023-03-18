package kim.hyunsub.common.api

import kim.hyunsub.common.api.model.EncodeParams
import org.springframework.core.env.Environment

class EncodeApiCaller(
	private val apiCaller: ApiCaller,
	environment: Environment,
) {
	private val url: String

	init {
	    val domainPrefix = if (environment.activeProfiles.contains("dev")) {
			"local-"
		} else {
			""
		}
		url = "https://${domainPrefix}encode.hyunsub.kim/api/v1/encode"
	}

	fun encode(params: EncodeParams) {
		apiCaller.post(url, params)
	}
}
