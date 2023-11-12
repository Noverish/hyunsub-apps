package kim.hyunsub.photo.mapper

import kim.hyunsub.photo.repository.entity.Photo
import org.apache.ibatis.annotations.Mapper
import org.springframework.data.domain.Pageable

@Mapper
interface PhotoMapper {
	fun count(userId: String, start: String, end: String): Int
	fun select(userId: String, start: String, end: String, page: Pageable): List<Photo>
}
