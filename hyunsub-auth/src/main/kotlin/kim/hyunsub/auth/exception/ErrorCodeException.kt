package kim.hyunsub.auth.exception

import kim.hyunsub.auth.model.ErrorCode

open class ErrorCodeException(
	val errorCode: ErrorCode,
	val payload: Any = emptyMap<String, String>(),
): RuntimeException()
