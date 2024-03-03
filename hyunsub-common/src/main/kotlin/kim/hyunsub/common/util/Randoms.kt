package kim.hyunsub.common.util

import kotlin.random.Random

private const val CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_"
private const val NUMBERS = "0123456789"

fun generateRandomString(size: Int, seed: Long = Random.nextLong()) = generateRandom(CHARS, size, seed)

fun generateRandomNumber(size: Int, seed: Long = Random.nextLong()) = generateRandom(NUMBERS, size, seed)

fun generateRandom(base: String, size: Int, seed: Long = System.nanoTime()): String {
	val random = Random(seed)

	return buildString {
		repeat(size) {
			val rand = ((random.nextLong() and 0xFFFFFFFFL) % base.length)
			append(base[rand.toInt()])
		}
	}
}
