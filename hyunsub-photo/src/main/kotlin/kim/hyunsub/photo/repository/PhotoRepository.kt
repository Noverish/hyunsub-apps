package kim.hyunsub.photo.repository

import kim.hyunsub.photo.repository.entity.Photo
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PhotoRepository: JpaRepository<Photo, Int> {
	fun findByAlbumIdOrderByDate(albumId: Int, page: Pageable = Pageable.unpaged()): List<Photo>
	fun countByAlbumId(albumId: Int): Int

	@Query(
		nativeQuery = true,
		value = """
			SELECT idx
			FROM (
			  SELECT `id`, (@row_number \:= @row_number + 1) AS idx
			  FROM photo, (SELECT @row_number \:= -1) r
			  WHERE album_id = :albumId
			  ORDER BY `date`
			) a
			WHERE `id` = :photoId
		""",
	)
	fun getIndexOfPhotoIdInAlbum(albumId: Int, photoId: Int): Int?
}
