package kim.hyunsub.common.fs.config

import feign.Logger
import feign.Response
import feign.Util
import mu.KotlinLogging

class FsClientLogger(clazz: Class<*>) : Logger() {
	private val log = KotlinLogging.logger(clazz.name)

	override fun log(configKey: String, format: String, vararg args: Any) {
		// Do nothing
	}

	override fun logAndRebufferResponse(configKey: String, logLevel: Level, response: Response, elapsedTime: Long): Response {
		if (!log.isDebugEnabled) {
			return response
		}

		val request = response.request()
		val method = request.httpMethod()
		val url = request.url()
		val reqBody = String(request.body() ?: byteArrayOf())
			.let { if (it.isNotEmpty()) " $it" else "" }

		val status = response.status()
		val resBody = response.body()
			?.let { Util.toByteArray(it.asInputStream()) }
			?: ByteArray(0)
		log.debug { "[FS] $method $url$reqBody --> ${elapsedTime}ms $status ${String(resBody)}" }
		return response.toBuilder().body(resBody).build()
	}
}
