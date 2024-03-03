package kim.hyunsub.photo.repository.mapper

import kim.hyunsub.common.database.MapperBase
import kim.hyunsub.common.database.generateId
import kim.hyunsub.photo.repository.condition.AlbumCondition
import kim.hyunsub.photo.repository.entity.Album
import org.apache.ibatis.annotations.Mapper

@Mapper
interface AlbumMapper : MapperBase {
	override fun countById(id: String): Int

	fun select(condition: AlbumCondition): List<Album>
	fun selectOne(id: String): Album?
	fun selectWithUserId(albumId: String, userId: String, owner: Boolean? = null): Album?

	fun insert(entity: Album): Int
	fun insertAll(entities: List<Album>): Int
	fun update(entity: Album): Int
	fun deleteById(id: String): Int
	fun deleteByIds(ids: List<String>): Int
}

fun AlbumMapper.generateId() = generateId(8)
