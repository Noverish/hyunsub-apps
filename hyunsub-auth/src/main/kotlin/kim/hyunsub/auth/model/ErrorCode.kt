package kim.hyunsub.auth.model

import org.springframework.http.HttpStatus


enum class ErrorCode(
	val code: Int,
	val msg: String,
	val status: HttpStatus = HttpStatus.BAD_REQUEST,
) {
	ALREADY_EXIST_USERNAME(1001, "Already exist username"),
	NOT_EXIST_USER(1002, "Invalid ID or password"),
}
