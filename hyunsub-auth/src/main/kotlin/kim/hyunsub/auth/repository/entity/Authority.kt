package kim.hyunsub.auth.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

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
