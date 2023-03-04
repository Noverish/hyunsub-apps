package kim.hyunsub.auth.repository.entity

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.Table

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
