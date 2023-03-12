package kim.hyunsub.photo.repository.entity

import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.Table

@Entity
@Table(name = "photo_owner")
@IdClass(PhotoOwnerId::class)
data class PhotoOwner(
	@Id
	val userId: String,

	@Id
	val photoId: String,

	@Column(nullable = false)
	val name: String,

	@Column(nullable = false)
	val regDt: LocalDateTime,
)

data class PhotoOwnerId(
	val userId: String = "",
	val photoId: String = "",
) : Serializable
