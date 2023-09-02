package kim.hyunsub.auth.repository.entity

import kim.hyunsub.auth.model.UserLanguage
import kim.hyunsub.auth.repository.converter.UserLanguageConverter
import javax.persistence.Column
import javax.persistence.Convert
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

	@Column
	@Convert(converter = UserLanguageConverter::class)
	val lang: UserLanguage? = null,
)
