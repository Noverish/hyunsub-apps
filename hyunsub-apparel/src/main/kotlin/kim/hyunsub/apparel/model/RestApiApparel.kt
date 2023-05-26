package kim.hyunsub.apparel.model

import kim.hyunsub.apparel.repository.entity.Apparel
import java.time.LocalDate
import java.time.LocalDateTime

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
	val discarded: Boolean,
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
	discarded = discardDt != null,
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
	discardDt = if (discarded) LocalDateTime.now() else null,
)

fun Apparel.copy(params: RestApiApparel) =
	this.copy(
		itemNo = params.itemNo,
		name = params.name,
		brand = params.brand,
		category = params.category,
		size = params.size,
		color = params.color,
		originPrice = params.originPrice,
		discountPrice = params.discountPrice,
		material = params.material,
		size2 = params.size2,
		buyDt = params.buyDt,
		buyLoc = params.buyLoc,
		makeDt = params.makeDt,
		discardDt = when {
			discardDt == null && params.discarded -> LocalDateTime.now()
			discardDt != null && !params.discarded -> null
			else -> discardDt
		}
	)
