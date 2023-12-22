package kim.hyunsub.photo.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.Table
import java.io.Serializable
import java.time.LocalDateTime

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
	val fileDt: LocalDateTime,

	@Column(nullable = false)
	val regDt: LocalDateTime,
)

data class PhotoOwnerId(
	val userId: String = "",
	val photoId: String = "",
) : Serializable
