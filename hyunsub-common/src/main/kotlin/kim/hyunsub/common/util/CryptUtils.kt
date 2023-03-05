package kim.hyunsub.common.util

import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.security.MessageDigest
import java.util.Base64

fun String.hashWithMD5(): String {
	val md = MessageDigest.getInstance("MD5")
	md.update(this.toByteArray(Charset.defaultCharset()))
	return md.digest().toHex()
}

// byte array <-> hex
fun ByteArray.toHex(): String = joinToString(separator = "") { eachByte -> "%02x".format(eachByte) }

fun String.decodeHex(): ByteArray {
	check(length % 2 == 0) { "Must have an even length" }

	return chunked(2)
		.map { it.toInt(16).toByte() }
		.toByteArray()
}

// byte array <-> base64
fun ByteArray.toBase64(): String = Base64.getUrlEncoder().encodeToString(this)

fun String.decodeBase64(): ByteArray = Base64.getUrlDecoder().decode(this)

// byte array <-> long
fun Long.toByteArray(bytes: Int = java.lang.Long.BYTES): ByteArray =
	ByteBuffer.allocate(bytes).also { it.putLong(this) }.array()

fun ByteArray.toLong(): Long =
	ByteBuffer.wrap(this).long
