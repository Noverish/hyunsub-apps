package kim.hyunsub.photo.repository.mapper

import kim.hyunsub.common.database.MapperBase
import kim.hyunsub.common.database.generateId
import kim.hyunsub.photo.repository.condition.PhotoCondition
import kim.hyunsub.photo.repository.condition.PhotoCondition2
import kim.hyunsub.photo.repository.entity.Photo
import kim.hyunsub.photo.repository.entity.PhotoPreview
import org.apache.ibatis.annotations.Mapper

@Mapper
interface PhotoMapper : MapperBase {
	override fun countById(id: String): Int

	fun select(condition: PhotoCondition): List<PhotoPreview>
	fun count(condition: PhotoCondition): Int
	fun selectOne(id: String, userId: String? = null): Photo?
	fun selectOne2(id: String, userId: String? = null): PhotoPreview?
	fun selectAlbumPhoto(condition: PhotoCondition2): List<PhotoPreview>
	fun countAlbumPhoto(condition: PhotoCondition2): Int
	fun selectByHash(hash: String): Photo?

	fun insert(entity: Photo): Int
	fun insertAll(entities: List<Photo>): Int
	fun updateId(from: String, to: String): Int
	fun deleteById(id: String): Int
}

fun PhotoMapper.generateId() = generateId(16)
