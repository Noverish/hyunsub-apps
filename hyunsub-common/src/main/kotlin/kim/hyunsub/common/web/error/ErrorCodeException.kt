package kim.hyunsub.common.web.error

open class ErrorCodeException(
	val errorCode: ErrorCode,
	val payload: Any? = null,
): RuntimeException(errorCode.msg)
