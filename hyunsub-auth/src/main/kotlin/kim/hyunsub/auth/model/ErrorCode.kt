package kim.hyunsub.auth.model

import org.springframework.http.HttpStatus

enum class ErrorCode(
	val code: Int,
	val msg: String,
	val status: HttpStatus = HttpStatus.BAD_REQUEST,
) {
	ALREADY_EXIST_USERNAME(1001, "Already exist username"),
	NOT_EXIST_USER(1002, "Invalid ID or password"),
	SHORT_USERNAME(1003, "ID should be longer than 3 characters"),
	SHORT_PASSWORD(1004, "Password must be longer than 7 characters"),

	NOT_LOGIN(2001, "Login required", HttpStatus.UNAUTHORIZED),
	INVALID_JWT(2002, "Invalid login information", HttpStatus.UNAUTHORIZED),
	EXPIRED_JWT(2003, "Expired login information", HttpStatus.UNAUTHORIZED),
	NO_SUCH_USER(2004, "No such user", HttpStatus.UNAUTHORIZED),
	CAPTCHA_REQUIRED(2011, "Captcha Required"),
	CAPTCHA_FAILURE(2012, "Captcha Failure"),
}
