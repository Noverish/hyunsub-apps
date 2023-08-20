package kim.hyunsub.diary

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["kim.hyunsub.diary", "kim.hyunsub.common"])
class HyunsubDiaryApplication

fun main(args: Array<String>) {
	runApplication<HyunsubDiaryApplication>(*args)
}
