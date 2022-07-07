package kim.hyunsub.common.random

import java.security.SecureRandom

private const val source = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_"
private val random = SecureRandom()

fun generateRandomString(size: Int) =
	Array(size) { source[random.nextInt(source.length)] }.joinToString("")
