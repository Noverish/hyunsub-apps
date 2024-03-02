package kim.hyunsub.photo.util

import kim.hyunsub.photo.repository.entity.Photo
import kim.hyunsub.photo.repository.entity.PhotoPreview
import kotlin.io.path.Path

object PhotoPathConverter {
	fun originalNew(photo: Photo) = path("original2", photo.idNew, photo.ext)
	fun thumbnailNew(photoIdNew: String) = path("thumbnail2", photoIdNew, "jpg")
	fun thumbnailNew(photo: Photo) = path("thumbnail2", photo.idNew, "jpg")
	fun thumbnailNew(photo: PhotoPreview) = path("thumbnail2", photo.idNew, "jpg")
	fun videoNew(photo: Photo) = path("video2", photo.idNew, "mp4")

	private fun path(type: String, photoIdNew: String, ext: String) = Path(
		"/hyunsub/photo/$type",
		photoIdNew.substring(IntRange(0, 0)),
		photoIdNew.substring(IntRange(1, 1)),
		"$photoIdNew.$ext",
	).toString()
}
