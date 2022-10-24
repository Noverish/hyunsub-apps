package kim.hyunsub.encode

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["kim.hyunsub.encode", "kim.hyunsub.common"])
class HyunsubEncodeApplication

fun main(args: Array<String>) {
	runApplication<HyunsubEncodeApplication>(*args)
}
