package kim.hyunsub.common.database

import kim.hyunsub.common.util.generateRandomString

interface MapperBase {
	fun countById(id: String): Int
}

fun MapperBase.generateId(length: Int): String {
	for (i in 0 until 3) {
		val id = generateRandomString(length)
		if (countById(id) == 0) {
			return id
		}
	}
	throw RuntimeException("Failed to generate new id")
}
