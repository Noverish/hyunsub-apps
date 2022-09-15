package kim.hyunsub.common.util

import com.fasterxml.jackson.databind.ObjectMapper

fun ObjectMapper.convertToMap(fromValue: Any): Map<String, Any> {
	val type = this.typeFactory.constructMapType(Map::class.java, String::class.java, Any::class.java)
	return this.convertValue(fromValue, type)
}
