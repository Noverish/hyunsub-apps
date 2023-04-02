package kim.hyunsub.photo.repository

import kim.hyunsub.photo.repository.entity.AlbumPhoto
import kim.hyunsub.photo.repository.entity.AlbumPhotoId
import kim.hyunsub.photo.repository.entity.PhotoV2
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface AlbumPhotoRepository : JpaRepository<AlbumPhoto, AlbumPhotoId> {
	@Query("""
		SELECT b FROM AlbumPhoto a
		INNER JOIN PhotoV2 b ON b.id = a.photoId
		WHERE a.albumId = :albumId
	""")
	fun findByAlbumId(albumId: String): List<PhotoV2>

	fun findByUserIdAndPhotoId(userId: String, photoId: String): List<AlbumPhoto>

	fun countByAlbumId(albumId: String): Int

	@Modifying
	@Query("UPDATE AlbumPhoto SET photoId = :to WHERE photoId = :from")
	fun updatePhotoId(from: String, to: String): Int
}
