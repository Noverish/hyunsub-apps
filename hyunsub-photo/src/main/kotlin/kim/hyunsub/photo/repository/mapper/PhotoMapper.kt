package kim.hyunsub.photo.repository.mapper

import kim.hyunsub.common.util.toMillis
import kim.hyunsub.photo.repository.condition.PhotoCondition
import kim.hyunsub.photo.repository.entity.Photo
import org.apache.ibatis.annotations.Mapper
import org.springframework.data.domain.Pageable
import java.time.OffsetDateTime

@Mapper
interface PhotoMapper {
	fun countById(id: String): Int
	fun count(condition: PhotoCondition): Int
	fun select(condition: PhotoCondition): List<Photo>
	fun selectOne(id: String, userId: String? = null): Photo?
	fun selectByAlbumId(albumId: String, page: Pageable = Pageable.unpaged()): List<Photo>

	fun insert(entity: Photo): Int
	fun insertAll(entities: List<Photo>): Int
	fun updateId(from: String, to: String): Int
	fun deleteById(id: String): Int
}

fun PhotoMapper.generateId(date: OffsetDateTime, hash: String): String {
	for (i in 0 until 3) {
		val id = Photo.generateId(date.toMillis(), hash, i)
		if (this.countById(id) == 0) {
			return id
		}
	}
	throw RuntimeException("Failed to generate new id")
}
