package kim.hyunsub.photo.repository.mapper

import kim.hyunsub.photo.repository.condition.PhotoOwnerCondition
import kim.hyunsub.photo.repository.entity.PhotoOwner
import org.apache.ibatis.annotations.Mapper

/**
 * PK: user_id, photo_id
 */
@Mapper
interface PhotoOwnerMapper {
	fun select(condition: PhotoOwnerCondition): List<PhotoOwner>
	fun count(condition: PhotoOwnerCondition): Int
	fun selectOne(userId: String, photoId: String): PhotoOwner?

	fun insert(entity: PhotoOwner): Int
	fun update(entity: PhotoOwner): Int
	fun updatePhotoId(from: String, to: String): Int
	fun upsertBulk(entities: List<PhotoOwner>): Int
	fun delete(entity: PhotoOwner): Int
	fun deleteAll(entities: List<PhotoOwner>): Int
}
