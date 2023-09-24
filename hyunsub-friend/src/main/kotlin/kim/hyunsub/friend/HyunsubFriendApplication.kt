package kim.hyunsub.friend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["kim.hyunsub.friend", "kim.hyunsub.common"])
class HyunsubFriendApplication

fun main(args: Array<String>) {
	runApplication<HyunsubFriendApplication>(*args)
}
