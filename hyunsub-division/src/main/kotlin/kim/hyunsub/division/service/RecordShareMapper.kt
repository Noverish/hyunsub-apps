package kim.hyunsub.division.service

import kim.hyunsub.division.model.dto.RestApiRecordShare
import kim.hyunsub.division.repository.entity.RecordShare
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers

@Mapper
interface RecordShareMapper {
	companion object {
		@JvmStatic
		val mapper: RecordShareMapper = Mappers.getMapper(RecordShareMapper::class.java)
	}

	fun convert(entity: RecordShare): RestApiRecordShare

	@Mapping(source = "recordId", target = "recordId")
	fun convert(dto: RestApiRecordShare, recordId: String): RecordShare
}
