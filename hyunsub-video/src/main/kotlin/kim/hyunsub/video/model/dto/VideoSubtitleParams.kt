package kim.hyunsub.video.model.dto

import org.springframework.web.multipart.MultipartFile

data class VideoSubtitleParams(
	val lang: String,
	val file: MultipartFile?,
	val path: String?,
	val override: Boolean = false,
)
