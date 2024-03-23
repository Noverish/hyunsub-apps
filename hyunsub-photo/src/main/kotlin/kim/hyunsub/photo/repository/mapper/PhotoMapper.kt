package kim.hyunsub.photo.repository.mapper

import kim.hyunsub.common.database.MapperBase
import kim.hyunsub.common.database.generateId
import kim.hyunsub.photo.repository.entity.Photo
import org.apache.ibatis.annotations.Mapper

@Mapper
interface PhotoMapper : MapperBase {
	override fun countById(id: String): Int

	fun selectByIds(ids: List<String>): List<Photo>
	fun selectOne(id: String): Photo?
	fun selectByHash(hash: String): Photo?

	fun insert(entity: Photo): Int
	fun deleteById(id: String): Int
	fun deleteByIds(ids: List<String>): Int
}

fun PhotoMapper.generateId() = generateId(16)
