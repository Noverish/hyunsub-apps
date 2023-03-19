package kim.hyunsub.photo.util

import kim.hyunsub.photo.repository.entity.PhotoV2
import kotlin.io.path.Path

object PhotoPathUtils {
	fun tmp(nonce: String) = Path("/hyunsub/file/upload", nonce).toString()
	fun original(photo: PhotoV2) = Path("/hyunsub/photo/original", photo.year.toString(), photo.fileName).toString()
	fun thumbnail(photoId: String) = Path("/hyunsub/photo/thumbnail", PhotoV2.parseYear(photoId).toString(), "$photoId.jpg").toString()
	fun video(photoId: String) = Path("/hyunsub/photo/video", PhotoV2.parseYear(photoId).toString(), "$photoId.mp4").toString()
}
