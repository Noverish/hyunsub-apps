package kim.hyunsub.photo.util

import kim.hyunsub.photo.repository.entity.Photo
import kim.hyunsub.photo.repository.entity.PhotoPreview
import kotlin.io.path.Path

object PhotoPathConverter {
	fun original(photo: Photo) = path("original2", photo.id, photo.ext)
	fun thumbnail(photoIdNew: String) = path("thumbnail2", photoIdNew, "jpg")
	fun thumbnail(photo: Photo) = path("thumbnail2", photo.id, "jpg")
	fun thumbnail(photo: PhotoPreview) = path("thumbnail2", photo.id, "jpg")
	fun video(photo: Photo) = path("video2", photo.id, "mp4")

	private fun path(type: String, photoIdNew: String, ext: String) = Path(
		"/hyunsub/photo/$type",
		photoIdNew.substring(IntRange(0, 0)),
		photoIdNew.substring(IntRange(1, 1)),
		"$photoIdNew.$ext",
	).toString()
}
