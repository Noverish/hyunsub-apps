package kim.hyunsub.video

import kim.hyunsub.common.fs.FsClient
import kim.hyunsub.common.fs.mkdir
import org.springframework.beans.factory.getBean
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["kim.hyunsub.video", "kim.hyunsub.common"])
class HyunsubVideoApplication

fun main(args: Array<String>) {
	val ctx = runApplication<HyunsubVideoApplication>(*args)

	val client: FsClient = ctx.getBean()

	val result = client.mkdir("/Videos/tmp")

	println(result)
}
