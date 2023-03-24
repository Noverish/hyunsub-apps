package kim.hyunsub.photo.util

import kotlin.io.path.Path
import kotlin.io.path.extension

fun isVideo(path: String) = Path(path).extension.lowercase() in listOf("mp4", "mov")

fun isImage(path: String) = Path(path).extension.lowercase() in listOf("jpg", "jpeg", "png")

fun isGif(path: String) = Path(path).extension.lowercase() == "gif"
