package kim.hyunsub.auth.repository.entity

import org.hibernate.annotations.Type
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "user")
data class User(
	@Id
	@Column(length = 8)
	val idNo: String,

	@Column(nullable = false)
	val username: String,

	@Column(nullable = false)
	val password: String,

	@Type(type = "char")
	@Column
	val lang: String? = null,
)
