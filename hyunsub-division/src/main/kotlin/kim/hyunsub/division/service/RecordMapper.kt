package kim.hyunsub.division.service

import kim.hyunsub.division.model.dto.RestApiRecord
import kim.hyunsub.division.repository.entity.Record
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.factory.Mappers

@Mapper
interface RecordMapper {
	companion object {
		@JvmStatic
		val mapper: RecordMapper = Mappers.getMapper(RecordMapper::class.java)
	}

	@Mapping(target = "shares", expression = "java(new java.util.ArrayList())")
	fun convert(entity: Record): RestApiRecord

	fun convert(dto: RestApiRecord): Record
}
