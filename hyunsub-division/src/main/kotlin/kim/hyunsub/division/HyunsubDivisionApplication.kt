package kim.hyunsub.division

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["kim.hyunsub.division", "kim.hyunsub.common"])
class HyunsubDivisionApplication

fun main(args: Array<String>) {
	runApplication<HyunsubDivisionApplication>(*args)
}
