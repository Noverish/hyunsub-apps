package kim.hyunsub.common.util

import java.lang.reflect.Method

inline fun <reified T> Method.getAnnotation(): T? {
	return annotations.filterIsInstance<T>().firstOrNull()
}

inline fun <reified T> Class<*>.getAnnotation(): T? {
	return annotations.filterIsInstance<T>().firstOrNull()
}
