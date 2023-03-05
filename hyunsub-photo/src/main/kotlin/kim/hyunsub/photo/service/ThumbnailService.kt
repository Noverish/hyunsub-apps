package kim.hyunsub.photo.service

import kim.hyunsub.common.api.ApiCaller
import kim.hyunsub.common.api.FileUrlConverter
import kim.hyunsub.common.api.model.ApiPhotoConvertParams
import kim.hyunsub.common.api.model.VideoThumbnailParams
import kim.hyunsub.common.util.hashWithMD5
import kim.hyunsub.photo.config.PhotoConstants
import kim.hyunsub.photo.util.isImage
import kim.hyunsub.photo.util.isVideo
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

	fun generateThumbnail(path: String) =
		generateThumbnail(path, path)

	fun generateThumbnail(originPath: String, targetPath: String): String {
		val thumbnailPath = getThumbnailPath(targetPath)

		if (isImage(originPath)) {
			apiCaller.imageConvert(
				ApiPhotoConvertParams(
					input = originPath,
					output = thumbnailPath,
					resize = "512x512>",
					quality = 60,
				)
			)
		} else if (isVideo(originPath)) {
			apiCaller.videoThumbnail(
				VideoThumbnailParams(
					input = originPath,
					output = thumbnailPath,
					time = 0.0,
				)
			)
		}

		return thumbnailPath
	}
}
