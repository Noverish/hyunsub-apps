package kim.hyunsub.auth.model.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include

@JsonInclude(Include.NON_NULL)
data class ProfileUpdateResult(
	val username: Boolean? = null,
	val password: Boolean? = null,
	val language: Boolean? = null,
)
