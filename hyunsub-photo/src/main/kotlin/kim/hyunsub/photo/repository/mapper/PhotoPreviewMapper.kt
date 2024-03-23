package kim.hyunsub.photo.repository.mapper

import kim.hyunsub.photo.repository.condition.PhotoOfAlbumCondition
import kim.hyunsub.photo.repository.condition.PhotoPreviewCondition
import kim.hyunsub.photo.repository.entity.PhotoPreview
import org.apache.ibatis.annotations.Mapper

@Mapper
interface PhotoPreviewMapper {
	fun select(condition: PhotoPreviewCondition): List<PhotoPreview>
	fun count(condition: PhotoPreviewCondition): Int
	fun selectAlbumPhoto(condition: PhotoOfAlbumCondition): List<PhotoPreview>
	fun countAlbumPhoto(condition: PhotoOfAlbumCondition): Int
	fun selectOne(id: String, userId: String): PhotoPreview?
}
