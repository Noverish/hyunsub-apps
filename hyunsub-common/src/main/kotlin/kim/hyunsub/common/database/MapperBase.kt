package kim.hyunsub.common.database

import kim.hyunsub.common.util.generateRandomString

interface MapperBase {
	fun count(id: String): Int
}

fun MapperBase.generateId(length: Int): String {
	for (i in 0 until 3) {
		val id = generateRandomString(length)
		if (count(id) == 0) {
			return id
		}
	}
	throw RuntimeException("Failed to generate new id")
}
