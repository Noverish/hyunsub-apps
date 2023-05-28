package kim.hyunsub.common.web.controller

import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.multipart.MaxUploadSizeExceededException

@RestControllerAdvice
class CommonControllerAdvice {
	private val log = KotlinLogging.logger { }

	@ExceptionHandler(ErrorCodeException::class)
	fun errorCodeException(ex: ErrorCodeException): ResponseEntity<Map<String, Any?>> =
		generateResponseEntity(ex.errorCode.status, ex.errorCode.code, ex.errorCode.msg, ex.payload)

	@ExceptionHandler(MethodArgumentTypeMismatchException::class)
	fun exception(ex: MethodArgumentTypeMismatchException): ResponseEntity<Map<String, Any?>> =
		generateResponseEntity(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_PARAMETER.code, ex.message)

	// 필수적인 Request Param이 없는 경우
	@ExceptionHandler(MissingServletRequestParameterException::class)
	fun missingServletRequestParameterException(ex: MissingServletRequestParameterException): ResponseEntity<Map<String, Any?>> =
		generateResponseEntity(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_PARAMETER.code, ex.message)

	// RequestBody가 포맷에 맞지 않는 경우
	@ExceptionHandler(HttpMessageNotReadableException::class)
	fun httpMessageNotReadableException(ex: HttpMessageNotReadableException): ResponseEntity<Map<String, Any?>> =
		generateResponseEntity(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_PARAMETER.code, ex.message)

	// multipart 파일 크키가 설정보다 넘어선 경우
	@ExceptionHandler(MaxUploadSizeExceededException::class)
	fun maxUploadSizeExceededException(ex: MaxUploadSizeExceededException): ResponseEntity<Map<String, Any?>> =
		generateResponseEntity(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_PARAMETER.code, ex.message)

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
