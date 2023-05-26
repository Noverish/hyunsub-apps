package kim.hyunsub.vestige

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["kim.hyunsub.vestige", "kim.hyunsub.common"])
class HyunsubVestigeApplication

fun main(args: Array<String>) {
	runApplication<HyunsubVestigeApplication>(*args)
}
