package kim.hyunsub.auth.service

import java.security.SecureRandom
import kotlin.math.pow

object IdNoGenerator {
	private const val ID_NO_LEN = 5
	private val modular = 10.0.pow(ID_NO_LEN.toDouble()).toInt()

	fun generate() = (SecureRandom().nextInt() % modular)
		.toString()
		.padStart(ID_NO_LEN, '0')
}
