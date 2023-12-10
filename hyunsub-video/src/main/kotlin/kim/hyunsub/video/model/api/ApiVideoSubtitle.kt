package kim.hyunsub.video.model.api

import kim.hyunsub.common.fs.FsPathConverter
import kim.hyunsub.video.repository.entity.VideoSubtitle
import kotlin.io.path.Path
import kotlin.io.path.nameWithoutExtension

data class ApiVideoSubtitle(
	val id: String,
	val url: String,
	val label: String,
	val srclang: String,
)

fun VideoSubtitle.toApi(): ApiVideoSubtitle {
	val fileName = Path(path).nameWithoutExtension
	val parts = fileName.split(".")
	val secondLast = parts[parts.size - 1]

	val code = Regex("^[a-z]{2}\\d?$").find(secondLast)?.value ?: "ko"

	val label = code
		.replace("en", "English ")
		.replace("ko", "Korean ")
		.replace("ja", "Japanese ")
		.trim()

	val url = Path(path)
		.let { "${it.parent}/${it.nameWithoutExtension}.vtt" }
		.let { FsPathConverter.convertToUrl(it) }

	return ApiVideoSubtitle(
		url = url,
		label = label,
		srclang = code,
		id = id,
	)
}
