package kim.hyunsub.common.log

import java.nio.charset.Charset
import java.security.MessageDigest

fun String.hashWithMD5(): String {
	val md = MessageDigest.getInstance("MD5")
	md.update(this.toByteArray(Charset.defaultCharset()))
	return md.digest().toHex()
}

fun ByteArray.toHex(): String = joinToString(separator = "") { eachByte -> "%02x".format(eachByte) }
