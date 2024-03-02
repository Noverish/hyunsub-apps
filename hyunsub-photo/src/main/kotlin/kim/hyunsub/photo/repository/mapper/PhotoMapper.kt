package kim.hyunsub.photo.repository.mapper

import kim.hyunsub.common.database.MapperBase
import kim.hyunsub.common.database.generateId
import kim.hyunsub.common.util.toMillis
import kim.hyunsub.photo.repository.condition.PhotoCondition
import kim.hyunsub.photo.repository.entity.Photo
import kim.hyunsub.photo.repository.entity.PhotoPreview
import org.apache.ibatis.annotations.Mapper
import org.springframework.data.domain.Pageable
import java.time.OffsetDateTime

@Mapper
interface PhotoMapper : MapperBase {
	override fun countById(id: String): Int

	fun count(condition: PhotoCondition): Int
	fun select(condition: PhotoCondition): List<Photo>
	fun select2(condition: PhotoCondition): List<PhotoPreview>
	fun selectOne(id: String, userId: String? = null): Photo?
	fun selectOne2(id: String, userId: String? = null): PhotoPreview?
	fun selectByAlbumId(albumId: String, page: Pageable = Pageable.unpaged()): List<Photo>
	fun selectByAlbumId2(albumId: String, page: Pageable = Pageable.unpaged()): List<PhotoPreview>

	fun insert(entity: Photo): Int
	fun insertAll(entities: List<Photo>): Int
	fun updateId(from: String, to: String): Int
	fun deleteById(id: String): Int
}

fun PhotoMapper.generateIdOld(date: OffsetDateTime, hash: String): String {
	for (i in 0 until 3) {
		val id = Photo.generateId(date.toMillis(), hash, i)
		if (this.countById(id) == 0) {
			return id
		}
	}
	throw RuntimeException("Failed to generate new id")
}

fun PhotoMapper.generateId() = generateId(16)
