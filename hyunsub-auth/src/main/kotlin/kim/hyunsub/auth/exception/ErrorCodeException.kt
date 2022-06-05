package kim.hyunsub.auth.exception

import kim.hyunsub.auth.model.ErrorCode

class ErrorCodeException(val errorCode: ErrorCode): RuntimeException()
