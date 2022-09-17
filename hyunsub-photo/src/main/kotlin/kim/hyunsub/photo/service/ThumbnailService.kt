package kim.hyunsub.photo.service

import kim.hyunsub.common.api.ApiCaller
import kim.hyunsub.common.api.FileUrlConverter
import kim.hyunsub.common.api.model.PhotoConvertParams
import kim.hyunsub.common.log.hashWithMD5
import kim.hyunsub.photo.config.PhotoConstants
import org.springframework.stereotype.Service
import java.nio.file.Paths

@Service
class ThumbnailService(
	private val fileUrlConverter: FileUrlConverter,
	private val apiCaller: ApiCaller,
) {
	fun getThumbnailPath(path: String) =
		path.hashWithMD5()
			.let { Paths.get(PhotoConstants.thumbnailDirPath, "$it.jpg").toString() }

	fun getThumbnailUrl(path: String?) =
		path?.hashWithMD5()
			?.let { Paths.get(PhotoConstants.thumbnailDirPath, "$it.jpg").toString() }
			?.let { fileUrlConverter.pathToUrl(it) }
			?: "/img/placeholder.jpg"

	fun generateThumbnail(path: String): String {
		val thumbnailPath = getThumbnailPath(path)
		apiCaller.imageConvert(PhotoConvertParams(
			input = path,
			output = thumbnailPath,
			resize = "512x512>",
			quality = 60,
		))
		return thumbnailPath
	}
}
