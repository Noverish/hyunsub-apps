package kim.hyunsub.common.util

import kim.hyunsub.common.database.MapperBase
import org.springframework.data.jpa.repository.JpaRepository

fun <T> JpaRepository<T, String>.generateId(length: Int): String {
	for (i in 0 until 3) {
		val id = generateRandomString(length)
		if (!existsById(id)) {
			return id
		}
	}
	throw RuntimeException("Failed to generate new id")
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
