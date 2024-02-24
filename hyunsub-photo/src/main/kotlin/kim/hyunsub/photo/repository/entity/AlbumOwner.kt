package kim.hyunsub.photo.repository.entity

import java.time.LocalDateTime

data class AlbumOwner(
	val albumId: String,
	val userId: String,
	val owner: Boolean,
	val regDt: LocalDateTime = LocalDateTime.now(),
)
