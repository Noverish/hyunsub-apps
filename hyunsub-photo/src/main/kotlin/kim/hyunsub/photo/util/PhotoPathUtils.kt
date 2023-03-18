package kim.hyunsub.photo.util

import kim.hyunsub.photo.repository.entity.PhotoV2
import kotlin.io.path.Path

object PhotoPathUtils {
	fun tmp(nonce: String) = Path("/hyunsub/file/upload", nonce).toString()
	fun original(photo: PhotoV2) = Path("/hyunsub/photo/original", photo.year.toString(), photo.fileName).toString()
	fun thumbnail(photo: PhotoV2) = Path("/hyunsub/photo/thumbnail", photo.year.toString(), "${photo.id}.jpg").toString()
}
