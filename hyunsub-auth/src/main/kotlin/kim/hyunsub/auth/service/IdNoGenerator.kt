package kim.hyunsub.auth.service

import java.lang.Math.pow
import java.security.SecureRandom
import kotlin.math.pow

object IdNoGenerator {
	private const val ID_NO_LEN = 5

	fun generate() = (SecureRandom().nextInt() % 10.0.pow(ID_NO_LEN.toDouble()))
		.toString()
		.padStart(ID_NO_LEN, '0')
}
