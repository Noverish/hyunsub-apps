package kim.hyunsub.apparel.model

import kim.hyunsub.apparel.repository.entity.ApparelPreview
import kim.hyunsub.apparel.service.ApparelPathConverter
import kim.hyunsub.common.api.FileUrlConverter

data class RestApiApparelPreview(
	val id: String,
	val name: String,
	val thumbnail: String,
)

fun ApparelPreview.toDto(userId: String) = RestApiApparelPreview(
	id = id,
	name = name,
	thumbnail = fileName
		?.let { ApparelPathConverter.imagePath(userId, id, it) }
		?.let { FileUrlConverter.convertToUrl(it) }
		?: "/img/placeholder.jpg",
)
