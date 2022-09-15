package kim.hyunsub.photo.repository.entity

import javax.persistence.*

@Entity
@Table(name = "photo_album")
data class Album(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Int = 0,

	@Column(nullable = false)
	val name: String,

	@Column(nullable = false)
	val thumbnail: String? = null,
)
