package kim.hyunsub.auth

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["kim.hyunsub.auth", "kim.hyunsub.common"])
class HyunsubAuthApplication

fun main(args: Array<String>) {
	runApplication<HyunsubAuthApplication>(*args)
}
