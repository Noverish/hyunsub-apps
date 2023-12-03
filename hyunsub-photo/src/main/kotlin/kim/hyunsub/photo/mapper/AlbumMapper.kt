package kim.hyunsub.photo.mapper

import kim.hyunsub.photo.repository.entity.Album
import org.apache.ibatis.annotations.Mapper
import org.springframework.data.domain.Pageable

@Mapper
interface AlbumMapper {
	fun selectList(userId: String, page: Pageable? = null, owner: Boolean? = null): List<Album>
	fun selectOne(userId: String, albumId: String, owner: Boolean? = null): Album?
}
