package kim.hyunsub.apparel.model

import kim.hyunsub.apparel.repository.entity.Apparel
import java.time.LocalDate

data class RestApiApparel(
	val id: String,
	val itemNo: String?,
	val name: String,
	val brand: String?,
	val category: String,
	val size: String?,
	val color: String?,
	val originPrice: Int?,
	val discountPrice: Int?,
	val material: String?,
	val size2: String?,
	val buyDt: LocalDate?,
	val buyLoc: String?,
	val makeDt: String?,
)

fun Apparel.toDto() = RestApiApparel(
	id = id,
	itemNo = itemNo,
	name = name,
	brand = brand,
	category = category,
	size = size,
	color = color,
	originPrice = originPrice,
	discountPrice = discountPrice,
	material = material,
	size2 = size2,
	buyDt = buyDt,
	buyLoc = buyLoc,
	makeDt = makeDt,
)

fun RestApiApparel.toEntity(id: String, userId: String, imageId: String?) = Apparel(
	id = id,
	userId = userId,
	itemNo = itemNo,
	name = name,
	brand = brand,
	category = category,
	size = size,
	color = color,
	originPrice = originPrice,
	discountPrice = discountPrice,
	material = material,
	size2 = size2,
	buyDt = buyDt,
	buyLoc = buyLoc,
	makeDt = makeDt,
	imageId = imageId,
)
