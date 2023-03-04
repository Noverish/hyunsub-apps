package kim.hyunsub.photo.repository.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "photo_album")
data class Album(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Int = 0,

	@Column(nullable = false)
	val name: String,

	@Column
	val thumbnail: String? = null,
)
