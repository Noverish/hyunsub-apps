package kim.hyunsub.comic.model

import kim.hyunsub.comic.config.ComicConstants
import kim.hyunsub.common.fs.FsPathConverter
import kotlin.io.path.Path

data class ApiComicPreview(
	val id: String,
	val title: String,
) {
	val thumbnail: String
		get() = FsPathConverter.convertToUrl(Path(ComicConstants.BASE_PATH, title, "thumbnail.jpg").toString())
}
