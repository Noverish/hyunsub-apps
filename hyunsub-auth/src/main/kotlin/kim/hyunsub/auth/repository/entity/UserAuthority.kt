package kim.hyunsub.auth.repository.entity

import javax.persistence.*

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
) : java.io.Serializable
