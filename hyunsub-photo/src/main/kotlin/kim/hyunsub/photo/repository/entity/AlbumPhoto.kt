package kim.hyunsub.photo.repository.entity

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.Table

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
