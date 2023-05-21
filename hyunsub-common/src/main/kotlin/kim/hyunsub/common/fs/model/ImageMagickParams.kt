package kim.hyunsub.common.fs.model

data class ImageMagickParams(
	val input: String,
	val output: String,
	val options: List<String>,
)
