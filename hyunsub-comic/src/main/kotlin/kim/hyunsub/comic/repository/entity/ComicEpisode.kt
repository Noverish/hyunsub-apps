package kim.hyunsub.comic.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.Table
import java.io.Serializable
import java.time.LocalDateTime

@Entity
@IdClass(ComicEpisodeId::class)
@Table(name = "comic_episode")
data class ComicEpisode(
	@Id
	val comicId: String,

	@Id
	val order: Int,

	@Column(nullable = false)
	val title: String,

	@Column(nullable = false)
	val length: Int,

	@Column(nullable = false)
	val regDt: LocalDateTime,
)

data class ComicEpisodeId(
	val comicId: String = "",
	val order: Int = 0,
) : Serializable
