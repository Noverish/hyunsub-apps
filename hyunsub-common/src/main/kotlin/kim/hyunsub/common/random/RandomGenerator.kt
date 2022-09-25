package kim.hyunsub.common.random

import org.springframework.stereotype.Service
import java.security.SecureRandom

@Service
class RandomGenerator {
	private val source = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
	private val sourceForId = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
	private val random = SecureRandom()

	fun generateRandomString(size: Int) =
		Array(size) { source[random.nextInt(source.length)] }.joinToString("")

	fun generateRandomId(size: Int) =
		Array(size) { sourceForId[random.nextInt(sourceForId.length)] }.joinToString("")
}
