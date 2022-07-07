package kim.hyunsub.common.web.controller

import kim.hyunsub.common.web.error.ErrorCodeException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CommonControllerAdvice {
	@ExceptionHandler(ErrorCodeException::class)
	fun errorCodeException(ex: ErrorCodeException): ResponseEntity<Map<String, Any?>> =
		ResponseEntity.status(ex.errorCode.status)
			.body(
				mapOf(
					"code" to ex.errorCode.code,
					"msg" to ex.errorCode.msg,
					"payload" to ex.payload,
				)
			)
}
