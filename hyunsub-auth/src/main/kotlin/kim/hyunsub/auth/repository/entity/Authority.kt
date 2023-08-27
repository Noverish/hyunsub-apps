package kim.hyunsub.auth.repository.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "authority")
data class Authority(
	@Id
	val id: Int,

	@Column(nullable = false)
	val name: String,

	@Column
	val path: String?,

	@Column
	val upload: String?,

	@Column
	val api: String?,

	@Column(nullable = false)
	val default: Boolean,
)
