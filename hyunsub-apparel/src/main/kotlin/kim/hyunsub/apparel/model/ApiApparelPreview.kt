package kim.hyunsub.apparel.model

import kim.hyunsub.apparel.repository.entity.ApparelPreview
import kim.hyunsub.apparel.service.ApparelPathConverter
import kim.hyunsub.common.fs.FsPathConverter

data class ApiApparelPreview(
	val id: String,
	val name: String,
	val thumbnail: String,
)

fun ApparelPreview.toApi(userId: String) = ApiApparelPreview(
	id = id,
	name = name,
	thumbnail = fileName
		?.let { ApparelPathConverter.imagePath(userId, id, it) }
		?.let { FsPathConverter.convertToUrl(it) }
		?: "/img/placeholder.jpg",
)