package kim.hyunsub.apparel.model

import kim.hyunsub.apparel.repository.entity.ApparelImage
import kim.hyunsub.apparel.service.ApparelPathConverter
import kim.hyunsub.common.fs.FsPathConverter

data class RestApiApparelImage(
	val imageId: String,
	val apparelId: String,
	val url: String,
)

fun ApparelImage.toDto(userId: String) = RestApiApparelImage(
	imageId = id,
	apparelId = apparelId,
	url = ApparelPathConverter.imagePath(userId, apparelId, fileName)
		.let { FsPathConverter.convertToUrl(it) }
)
