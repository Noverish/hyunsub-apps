package kim.hyunsub.dutch.model

import com.fasterxml.jackson.annotation.JsonValue
import kim.hyunsub.common.database.DatabaseEnum
import kim.hyunsub.common.database.DatabaseEnumTypeHandler
import org.apache.ibatis.type.MappedTypes

enum class DutchPayment : DatabaseEnum {
	CASH,
	CARD,
	;

	@get:JsonValue
	override val value: String
		get() = this.name
}

@MappedTypes(DutchPayment::class)
class DutchPaymentTypeHandler : DatabaseEnumTypeHandler<DutchPayment>(DutchPayment::class.java)
