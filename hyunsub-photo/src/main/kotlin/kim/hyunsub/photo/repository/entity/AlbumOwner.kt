package kim.hyunsub.photo.repository.entity

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.Table

@Entity
@Table(name = "album_owner")
@IdClass(AlbumOwnerId::class)
data class AlbumOwner(
	@Id
	val albumId: String,

	@Id
	val userId: String,

	@Column(nullable = false)
	val owner: Boolean,
)

data class AlbumOwnerId(
	val albumId: String = "",
	val userId: String = "",
) : Serializable
