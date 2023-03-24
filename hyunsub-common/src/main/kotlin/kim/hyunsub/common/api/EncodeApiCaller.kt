package kim.hyunsub.common.api

import kim.hyunsub.common.api.model.EncodeParams

class EncodeApiCaller(
	private val apiCaller: ApiCaller,
) {
	private val url: String = "https://encode.hyunsub.kim/api/v1/encode"

	fun encode(params: EncodeParams) {
		apiCaller.post(url, params)
	}
}
