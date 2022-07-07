package kim.hyunsub.video

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["kim.hyunsub.video", "kim.hyunsub.common"])
class HyunsubVideoApplication

fun main(args: Array<String>) {
	runApplication<HyunsubVideoApplication>(*args)
}
