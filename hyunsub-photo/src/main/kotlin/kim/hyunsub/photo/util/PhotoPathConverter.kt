package kim.hyunsub.photo.util

import kim.hyunsub.photo.repository.entity.Photo
import kim.hyunsub.photo.repository.entity.PhotoPreview
import kotlin.io.path.Path

object PhotoPathConverter {
	fun original(photo: Photo) = generatePath("original2", photo.id, photo.ext)
	fun thumbnail(photoId: String) = generatePath("thumbnail2", photoId, "jpg")
	fun thumbnail(photo: Photo) = generatePath("thumbnail2", photo.id, "jpg")
	fun thumbnail(photo: PhotoPreview) = generatePath("thumbnail2", photo.id, "jpg")
	fun video(photo: Photo) = generatePath("video2", photo.id, "mp4")

	private fun generatePath(type: String, photoIdNew: String, ext: String) = Path(
		"/hyunsub/photo/$type",
		photoIdNew.substring(IntRange(0, 0)),
		photoIdNew.substring(IntRange(1, 1)),
		"$photoIdNew.$ext",
	).toString()
}
