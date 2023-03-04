package kim.hyunsub.comic.repository.entity

import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.Table

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
