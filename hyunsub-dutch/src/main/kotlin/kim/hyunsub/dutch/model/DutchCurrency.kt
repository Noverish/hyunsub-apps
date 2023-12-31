package kim.hyunsub.dutch.model

import com.fasterxml.jackson.annotation.JsonValue
import kim.hyunsub.common.database.DatabaseEnum
import kim.hyunsub.common.database.DatabaseEnumTypeHandler
import org.apache.ibatis.type.MappedTypes

enum class DutchCurrency : DatabaseEnum {
	KRW,
	JPY,
	USD,
	CNY,
	TWD,
	;

	@get:JsonValue
	override val value: String
		get() = this.name
}

@MappedTypes(DutchCurrency::class)
class DutchCurrencyTypeHandler : DatabaseEnumTypeHandler<DutchCurrency>(DutchCurrency::class.java)
