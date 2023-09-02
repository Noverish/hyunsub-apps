package kim.hyunsub.auth.repository.converter

import kim.hyunsub.auth.model.UserLanguage
import javax.persistence.AttributeConverter

class UserLanguageConverter : AttributeConverter<UserLanguage, String> {
	override fun convertToDatabaseColumn(attribute: UserLanguage?): String? =
		attribute?.value

	override fun convertToEntityAttribute(dbData: String?): UserLanguage? =
		UserLanguage.values().firstOrNull { it.value == dbData }
}
