package kim.hyunsub.photo.repository.entity

import kim.hyunsub.common.fs.FsPathConverter
import kim.hyunsub.photo.model.api.ApiAlbumPreview
import kim.hyunsub.photo.util.PhotoPathConverter
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "album")
data class Album(
	@Id
	@Column(length = 8)
	val id: String,

	@Column(nullable = false)
	val name: String,

	@Column
	val thumbnailPhotoId: String? = null,

	@Column(nullable = false)
	val regDt: LocalDateTime = LocalDateTime.now(),
) {
	fun toPreview() = ApiAlbumPreview(
		id = id,
		name = name,
		thumbnail = FsPathConverter.thumbnailUrl(thumbnailPhotoId?.let { PhotoPathConverter.thumbnail(it) }),
	)
}
