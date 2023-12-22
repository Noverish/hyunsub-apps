package kim.hyunsub.comic.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "comic")
data class Comic(
	@Id
	val id: String,

	@Column(nullable = false)
	val title: String,

	@Column(nullable = false)
	val authority: String,
)
