package kim.hyunsub.dutch

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication(scanBasePackages = ["kim.hyunsub.dutch", "kim.hyunsub.common"])
class HyunsubDutchApplication

fun main(args: Array<String>) {
	runApplication<HyunsubDutchApplication>(*args)
}
