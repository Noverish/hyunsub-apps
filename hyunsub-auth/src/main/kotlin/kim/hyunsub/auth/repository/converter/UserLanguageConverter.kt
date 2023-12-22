package kim.hyunsub.auth.repository.converter

import jakarta.persistence.AttributeConverter
import kim.hyunsub.auth.model.UserLanguage

class UserLanguageConverter : AttributeConverter<UserLanguage, String> {
	override fun convertToDatabaseColumn(attribute: UserLanguage?): String? =
		attribute?.value

	override fun convertToEntityAttribute(dbData: String?): UserLanguage? =
		UserLanguage.values().firstOrNull { it.value == dbData }
}
