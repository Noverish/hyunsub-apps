package kim.hyunsub.comic.repository.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "comic")
data class Comic(
	@Id
	val id: String,

	@Column(nullable = false)
	val title: String,
)
