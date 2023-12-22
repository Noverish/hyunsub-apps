package kim.hyunsub.photo.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.Table
import java.io.Serializable

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
