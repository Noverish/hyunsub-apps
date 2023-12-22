package kim.hyunsub.photo.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.Table
import java.io.Serializable

@Entity
@Table(name = "album_photo")
@IdClass(AlbumPhotoId::class)
data class AlbumPhoto(
	@Id
	val albumId: String,

	@Id
	val photoId: String,

	@Column(nullable = false)
	val userId: String,
)

data class AlbumPhotoId(
	val albumId: String = "",
	val photoId: String = "",
) : Serializable
