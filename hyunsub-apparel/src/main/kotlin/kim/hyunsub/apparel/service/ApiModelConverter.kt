package kim.hyunsub.apparel.service

import kim.hyunsub.apparel.model.RestApiApparelPreview
import kim.hyunsub.apparel.repository.entity.ApparelPreview
import kim.hyunsub.common.api.FileUrlConverter
import org.springframework.stereotype.Service

@Service
class ApiModelConverter {
	fun convert(userId: String, apparel: ApparelPreview): RestApiApparelPreview {
		val url = apparel.fileName
			?.let { ApparelPathConverter.imagePath(userId, apparel.id, it) }
			?.let { FileUrlConverter.convertToUrl(it) }
			?: "/img/placeholder.jpg"

		return RestApiApparelPreview(
			id = apparel.id,
			name = apparel.name,
			thumbnail = url,
		)
	}
}
