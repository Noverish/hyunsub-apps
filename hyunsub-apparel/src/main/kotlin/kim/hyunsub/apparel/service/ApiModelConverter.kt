package kim.hyunsub.apparel.service

import kim.hyunsub.apparel.model.RestApiApparel
import kim.hyunsub.apparel.model.RestApiApparelImage
import kim.hyunsub.apparel.model.RestApiApparelPreview
import kim.hyunsub.apparel.repository.entity.Apparel
import kim.hyunsub.apparel.repository.entity.ApparelImage
import kim.hyunsub.apparel.repository.entity.ApparelPreview
import kim.hyunsub.common.api.FileUrlConverter
import org.springframework.stereotype.Service

@Service
class ApiModelConverter(
	private val fileUrlConverter: FileUrlConverter,
) {
	fun convert(userId: String, apparel: ApparelPreview): RestApiApparelPreview {
		val url = apparel.fileName
			?.let { FilePathConverter.getApparelImagePath(userId, apparel.id, it) }
			?.let { fileUrlConverter.pathToUrl(it) }
			?: "/img/placeholder.jpg"

		return RestApiApparelPreview(
			id = apparel.id,
			name = apparel.name,
			thumbnail = url,
		)
	}

	fun convert(userId: String, image: ApparelImage): RestApiApparelImage {
		val path = FilePathConverter.getApparelImagePath(userId, image.apparelId, image.fileName)
		val url = fileUrlConverter.pathToUrl(path)

		return RestApiApparelImage(
			imageId = image.id,
			apparelId = image.apparelId,
			url = url,
		)
	}

	fun convert(userId: String, apparel: Apparel, images: List<ApparelImage>): RestApiApparel {
		return RestApiApparel(
			id = apparel.id,
			itemNo = apparel.itemNo,
			name = apparel.name,
			brand = apparel.brand,
			category = apparel.category,
			size = apparel.size,
			color = apparel.color,
			originPrice = apparel.originPrice,
			discountPrice = apparel.discountPrice,
			material = apparel.material,
			size2 = apparel.size2,
			buyDt = apparel.buyDt,
			buyLoc = apparel.buyLoc,
			makeDt = apparel.makeDt,
			images = images.map { convert(userId, it) },
		)
	}
}
