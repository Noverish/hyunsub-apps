package kim.hyunsub.apparel

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["kim.hyunsub.apparel", "kim.hyunsub.common"])
class HyunsubApparelApplication

fun main(args: Array<String>) {
	runApplication<HyunsubApparelApplication>(*args)
}
