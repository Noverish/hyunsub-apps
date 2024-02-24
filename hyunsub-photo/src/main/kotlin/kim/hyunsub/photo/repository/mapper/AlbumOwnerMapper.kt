package kim.hyunsub.photo.repository.mapper

import kim.hyunsub.photo.repository.condition.AlbumOwnerCondition
import kim.hyunsub.photo.repository.entity.AlbumOwner
import org.apache.ibatis.annotations.Mapper

@Mapper
interface AlbumOwnerMapper {
	fun select(condition: AlbumOwnerCondition): List<AlbumOwner>
	fun count(condition: AlbumOwnerCondition): Int
	fun selectOne(albumId: String, userId: String): AlbumOwner?

	fun insert(entity: AlbumOwner): Int
	fun insertAll(entities: List<AlbumOwner>): Int
	fun delete(entity: AlbumOwner): Int
	fun deleteAll(entities: List<AlbumOwner>): Int
	fun deleteByAlbumId(albumId: String): Int
}
