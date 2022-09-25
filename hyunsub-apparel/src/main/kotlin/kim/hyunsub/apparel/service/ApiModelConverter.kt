package kim.hyunsub.apparel.service

import kim.hyunsub.apparel.model.RestApiApparel
import kim.hyunsub.apparel.repository.entity.Apparel
import kim.hyunsub.apparel.repository.entity.ApparelPhoto
import kim.hyunsub.common.api.FileUrlConverter
import org.springframework.stereotype.Service

@Service
class ApiModelConverter(
	private val fileUrlConverter: FileUrlConverter,
) {
	fun convert(userId: String, apparel: Apparel): RestApiApparel {
		val path = "/hyunsub/apparel/apparel/$userId/${apparel.id}/${apparel.thumbnail}"
		val url = fileUrlConverter.pathToUrl(path)

		return RestApiApparel(
			id = apparel.id,
			itemId = apparel.itemId,
			name = apparel.name,
			brand = apparel.brand,
			size = apparel.size,
			color = apparel.color,
			originPrice = apparel.originPrice,
			discountPrice = apparel.discountPrice,
			buyDt = apparel.buyDt,
			buyLoc = apparel.buyLoc,
			makeDt = apparel.makeDt,
			photos = listOf(url),
		)
	}

	fun convert(userId: String, apparel: Apparel, photos: List<ApparelPhoto>): RestApiApparel {
		val urls = photos
			.map { "/hyunsub/apparel/apparel/$userId/${apparel.id}/${it.fileName}" }
			.map { fileUrlConverter.pathToUrl(it) }

		return RestApiApparel(
			id = apparel.id,
			itemId = apparel.itemId,
			name = apparel.name,
			brand = apparel.brand,
			size = apparel.size,
			color = apparel.color,
			originPrice = apparel.originPrice,
			discountPrice = apparel.discountPrice,
			buyDt = apparel.buyDt,
			buyLoc = apparel.buyLoc,
			makeDt = apparel.makeDt,
			photos = urls,
		)
	}
}
