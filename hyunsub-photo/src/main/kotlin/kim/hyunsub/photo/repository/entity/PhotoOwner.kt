package kim.hyunsub.photo.repository.entity

import java.time.LocalDateTime

data class PhotoOwner(
	val userId: String,
	val photoId: String,
	val name: String,
	val fileDt: LocalDateTime,
	val regDt: LocalDateTime,
)
