package kim.hyunsub.common.util

import com.fasterxml.jackson.databind.ObjectMapper
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

fun ObjectMapper.convertToMap(fromValue: Any): Map<String, Any> {
	val type = this.typeFactory.constructMapType(Map::class.java, String::class.java, Any::class.java)
	return this.convertValue(fromValue, type)
}

@OptIn(ExperimentalContracts::class)
fun CharSequence?.isNotEmpty(): Boolean {
	contract {
		returns(true) implies (this@isNotEmpty != null)
	}
	return !this.isNullOrEmpty()
}
