package kim.hyunsub.photo.repository.mapper

import kim.hyunsub.photo.repository.condition.AlbumPhotoCondition
import kim.hyunsub.photo.repository.entity.AlbumPhoto
import org.apache.ibatis.annotations.Mapper

@Mapper
interface AlbumPhotoMapper {
	fun select(condition: AlbumPhotoCondition): List<AlbumPhoto>
	fun count(condition: AlbumPhotoCondition): Int
	fun selectOne(albumId: String, photoId: String): AlbumPhoto?
	fun indexOfPhoto(albumId: String, photoId: String): Int

	fun insert(entity: AlbumPhoto): Int
	fun insertAll(entities: List<AlbumPhoto>): Int
	fun delete(condition: AlbumPhotoCondition): Int
}
