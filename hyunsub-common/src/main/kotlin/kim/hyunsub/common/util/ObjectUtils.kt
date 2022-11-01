package kim.hyunsub.common.util

import com.fasterxml.jackson.databind.ObjectMapper
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor

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

/**
 * Merge two data classes
 *
 * The resulting data class will contain:
 * - all fields of `other` which are non-null
 * - the fields of `this` for the fields which are null in `other`
 *
 * from: https://gist.github.com/josdejong/fbb43ae33fcdd922040dac4ffc31aeaf
 */
inline infix fun <reified T : Any> T.merge(other: T): T {
	val nameToProperty = T::class.declaredMemberProperties.associateBy { it.name }
	val primaryConstructor = T::class.primaryConstructor!!
	val args = primaryConstructor.parameters.associateWith { parameter ->
		val property = nameToProperty[parameter.name]!!
		(property.get(other) ?: property.get(this))
	}
	return primaryConstructor.callBy(args)
}
