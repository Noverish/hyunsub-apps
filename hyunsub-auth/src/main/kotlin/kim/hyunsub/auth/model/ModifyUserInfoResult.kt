package kim.hyunsub.auth.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include

@JsonInclude(Include.NON_NULL)
data class ModifyUserInfoResult(
	val username: Boolean? = null,
	val password: Boolean? = null,
)
