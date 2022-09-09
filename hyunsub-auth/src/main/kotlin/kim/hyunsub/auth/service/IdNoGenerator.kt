package kim.hyunsub.auth.service

import java.security.SecureRandom
import kotlin.math.pow

object IdNoGenerator {
	const val ID_NO_LEN = 5
	private val limit = 10.0.pow(ID_NO_LEN.toDouble()).toInt()

	fun generate() = (SecureRandom().nextInt(limit))
		.toString()
		.padStart(ID_NO_LEN, '0')
}
