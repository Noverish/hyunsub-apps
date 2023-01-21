package kim.hyunsub.comic.repository.entity

import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@IdClass(ComicHistoryId::class)
@Table(name = "comic_history")
data class ComicHistory(
	@Id
	val userId: String,

	@Id
	val comicId: String,

	@Id
	val order: Int,

	@Column(nullable = false)
	val page: Int = 0,

	@Column(nullable = false)
	val date: LocalDateTime = LocalDateTime.now(),
)

data class ComicHistoryId(
	val userId: String = "",
	val comicId: String = "",
	val order: Int = 0,
) : Serializable
