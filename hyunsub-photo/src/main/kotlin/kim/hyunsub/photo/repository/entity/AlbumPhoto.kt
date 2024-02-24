package kim.hyunsub.photo.repository.entity

import java.time.LocalDateTime

data class AlbumPhoto(
	val albumId: String,
	val photoId: String,
	val userId: String,
	val regDt: LocalDateTime = LocalDateTime.now(),
)
