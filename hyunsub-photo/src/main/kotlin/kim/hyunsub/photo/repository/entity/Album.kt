package kim.hyunsub.photo.repository.entity

import javax.persistence.*

@Entity
@Table(name = "photo_album")
data class Album(
	@Id
	val id: Int,

	@Column(nullable = false)
	val name: String,

	@Column(nullable = false)
	val thumbnail: String
)
