package kim.hyunsub.common.web.controller

import kim.hyunsub.common.log.Log
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@RestControllerAdvice
class CommonControllerAdvice {
	companion object : Log

	@ExceptionHandler(ErrorCodeException::class)
	fun errorCodeException(ex: ErrorCodeException): ResponseEntity<Map<String, Any?>> =
		generateResponseEntity(ex.errorCode.status, ex.errorCode.code, ex.errorCode.msg, ex.payload)

	@ExceptionHandler(MethodArgumentTypeMismatchException::class)
	fun exception(ex: MethodArgumentTypeMismatchException): ResponseEntity<Map<String, Any?>> =
		generateResponseEntity(HttpStatus.BAD_REQUEST, ErrorCode.METHOD_ARGUMENT_TYPE_MISMATCH.code, ex.message)

	@ExceptionHandler(Exception::class)
	fun exception(ex: Exception): ResponseEntity<Map<String, Any?>> {
		log.error("Unexpected Error: {}", ex.message, ex)
		return generateResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR.code, ex.message)
	}

	fun generateResponseEntity(
		status: HttpStatus,
		code: Int,
		msg: String?,
		payload: Any? = null,
	): ResponseEntity<Map<String, Any?>> {
		return ResponseEntity.status(status)
			.body(
				mapOf(
					"code" to code,
					"msg" to msg,
					"payload" to payload
				)
			)
	}
}
