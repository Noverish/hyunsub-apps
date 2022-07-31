package kim.hyunsub.common.web.error

import org.springframework.http.HttpStatus

enum class ErrorCode(
	val code: Int,
	val msg: String,
	val status: HttpStatus = HttpStatus.BAD_REQUEST,
) {
	INTERNAL_SERVER_ERROR(101, "INTERNAL_SERVER_ERROR"),
	METHOD_ARGUMENT_TYPE_MISMATCH(102, "METHOD_ARGUMENT_TYPE_MISMATCH"),
	NOT_FOUND(103, "NOT_FOUND", HttpStatus.NOT_FOUND),

	ALREADY_EXIST_USERNAME(1001, "Already exist username"),
	NOT_EXIST_USER(1002, "Invalid ID or password"),
	SHORT_USERNAME(1003, "ID should be longer than 3 characters"),
	SHORT_PASSWORD(1004, "Password must be longer than 7 characters"),

	NOT_LOGIN(2001, "Login Required", HttpStatus.UNAUTHORIZED),
	INVALID_JWT(2002, "Invalid Login Information", HttpStatus.UNAUTHORIZED),
	EXPIRED_JWT(2003, "Expired Login Information", HttpStatus.UNAUTHORIZED),
	NO_SUCH_USER(2004, "No Such User", HttpStatus.UNAUTHORIZED),
	INVALID_URL(2005, "Invalid URL", HttpStatus.UNAUTHORIZED),
	NO_USER_AUTH(2006, "Auth Header Not Exist", HttpStatus.UNAUTHORIZED),
	INVALID_USER_AUTH(2007, "Invalid Auth Header", HttpStatus.UNAUTHORIZED),
	NO_AUTHORITY(2008, "No Authority", HttpStatus.FORBIDDEN),
	CAPTCHA_REQUIRED(2011, "Captcha Required"),
	CAPTCHA_FAILURE(2012, "Captcha Failure"),
}
