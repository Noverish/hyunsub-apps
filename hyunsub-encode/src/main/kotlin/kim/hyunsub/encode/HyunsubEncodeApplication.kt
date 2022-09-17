package kim.hyunsub.encode

import kim.hyunsub.common.api.ApiCaller
import kim.hyunsub.common.http.HttpClient
import kim.hyunsub.encode.repository.EncodeRepository
import kim.hyunsub.encode.service.EncodeThread
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["kim.hyunsub.encode", "kim.hyunsub.common"])
class HyunsubEncodeApplication

fun main(args: Array<String>) {
	val ctx = runApplication<HyunsubEncodeApplication>(*args)

	val encodeRepository = ctx.getBean(EncodeRepository::class.java)
	val apiCaller = ctx.getBean(ApiCaller::class.java)
	val httpClient = ctx.getBean(HttpClient::class.java)
	EncodeThread(encodeRepository, apiCaller, httpClient, true).start()
}
