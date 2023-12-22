package kim.hyunsub.auth.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import kim.hyunsub.auth.model.UserLanguage
import kim.hyunsub.auth.repository.converter.UserLanguageConverter

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

	@Column
	@Convert(converter = UserLanguageConverter::class)
	val lang: UserLanguage? = null,
)
