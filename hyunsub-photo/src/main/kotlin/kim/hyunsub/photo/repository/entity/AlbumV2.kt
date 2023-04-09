package kim.hyunsub.photo.repository.entity

import kim.hyunsub.common.api.FileUrlConverter
import kim.hyunsub.photo.model.api.RestApiAlbumPreview
import kim.hyunsub.photo.util.PhotoPathUtils
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "album")
data class AlbumV2(
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
	fun toPreview() = RestApiAlbumPreview(
		id = id,
		name = name,
		thumbnail = FileUrlConverter.thumbnailUrl(thumbnailPhotoId?.let { PhotoPathUtils.thumbnail(it) }),
	)
}
