package kim.hyunsub.photo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["kim.hyunsub.photo", "kim.hyunsub.common"])
class HyunsubPhotoApplication

fun main(args: Array<String>) {
	runApplication<HyunsubPhotoApplication>(*args)
}
