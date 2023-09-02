package kim.hyunsub.auth.model.dto

import kim.hyunsub.auth.model.UserLanguage

data class ProfileUpdateParams(
	val username: String?,
	val password: String?,
	val language: UserLanguage?,
)
