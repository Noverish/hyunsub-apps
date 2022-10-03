package kim.hyunsub.common.api

import kim.hyunsub.common.api.model.EncodeParams
import org.springframework.stereotype.Service

class EncodeApiCaller(
	private val apiCaller: ApiCaller,
	apiProperties: ApiProperties,
) {
	private val url = "https://${apiProperties.encodeHost}/api/v1/encode"

	fun encode(params: EncodeParams) {
		apiCaller.post(url, params)
	}
}
