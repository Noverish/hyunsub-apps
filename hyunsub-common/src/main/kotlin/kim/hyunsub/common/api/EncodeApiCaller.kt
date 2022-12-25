package kim.hyunsub.common.api

import kim.hyunsub.common.api.model.ApiEncodeParams

class EncodeApiCaller(
	private val apiCaller: ApiCaller,
) {
	private val url = "https://encode.hyunsub.kim/api/v1/encode"

	fun encode(params: ApiEncodeParams) {
		apiCaller.post(url, params)
	}
}
