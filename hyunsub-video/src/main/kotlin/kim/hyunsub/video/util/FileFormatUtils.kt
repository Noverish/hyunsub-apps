package kim.hyunsub.video.util

import kotlin.io.path.Path
import kotlin.io.path.extension

fun isVideo(path: String) = Path(path).extension.lowercase() in listOf("mp4", "webm")

fun isSubtitle(path: String) = Path(path).extension.lowercase() in listOf("srt", "smi", "vtt")
