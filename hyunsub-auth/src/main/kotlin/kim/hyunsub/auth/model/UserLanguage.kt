package kim.hyunsub.auth.model

import com.fasterxml.jackson.annotation.JsonValue

enum class UserLanguage(@JsonValue val value: String) {
	KO("ko"),
	EN("en"),
	;
}
