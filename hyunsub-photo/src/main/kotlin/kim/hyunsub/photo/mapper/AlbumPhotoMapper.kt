package kim.hyunsub.photo.mapper

import kim.hyunsub.photo.mapper.entity.AlbumPhoto
import org.apache.ibatis.annotations.Mapper

@Mapper
interface AlbumPhotoMapper {
	fun selectByPhotoId(photoId: String): List<AlbumPhoto>
}
