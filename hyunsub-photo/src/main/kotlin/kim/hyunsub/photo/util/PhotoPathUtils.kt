package kim.hyunsub.photo.util

import kim.hyunsub.photo.repository.entity.Photo
import kotlin.io.path.Path

object PhotoPathUtils {
	fun tmp(nonce: String) = Path("/hyunsub/file/upload", nonce).toString()
	fun original(photo: Photo) = Path("/hyunsub/photo/original", photo.year.toString(), photo.fileName).toString()
	fun thumbnail(photoId: String) = Path("/hyunsub/photo/thumbnail", Photo.parseYear(photoId).toString(), "$photoId.jpg").toString()
	fun video(photoId: String) = Path("/hyunsub/photo/video", Photo.parseYear(photoId).toString(), "$photoId.mp4").toString()
}
