package kim.hyunsub.drive

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["kim.hyunsub.drive", "kim.hyunsub.common"], exclude = [DataSourceAutoConfiguration::class])
class HyunsubDriveApplication

fun main(args: Array<String>) {
	runApplication<HyunsubDriveApplication>(*args)
}
