package kim.hyunsub.comic

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["kim.hyunsub.comic", "kim.hyunsub.common"])
class HyunsubComicApplication

fun main(args: Array<String>) {
	runApplication<HyunsubComicApplication>(*args)
}
