package kim.hyunsub.auth.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.Table
import java.io.Serializable

@Entity
@Table(name = "user_authority")
@IdClass(UserAuthorityId::class)
data class UserAuthority(
	@Id
	@Column(length = 8)
	val userIdNo: String,

	@Id
	val authorityId: Int,
)

data class UserAuthorityId(
	val userIdNo: String = "",
	val authorityId: Int = -1,
) : Serializable
