package kim.hyunsub.photo.repository

import kim.hyunsub.photo.model.api.ApiPhotoMetadata
import kim.hyunsub.photo.repository.entity.AlbumPhoto
import kim.hyunsub.photo.repository.entity.AlbumPhotoId
import kim.hyunsub.photo.repository.entity.Photo
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface AlbumPhotoRepository : JpaRepository<AlbumPhoto, AlbumPhotoId> {
	@Query(
		"""
			SELECT b FROM AlbumPhoto a
			INNER JOIN Photo b ON b.id = a.photoId
			WHERE a.albumId = :albumId
		"""
	)
	fun findByAlbumId(albumId: String, page: Pageable = Pageable.unpaged()): List<Photo>

	@Query(
		"""
			SELECT new kim.hyunsub.photo.model.api.ApiPhotoMetadata(a.photoId, d.offset, d.dateType, a.userId, b.name, b.fileDt, c.SubSecDateTimeOriginal, c.DateTimeOriginal, c.GPSDateTime, c.TimeStamp, c.ModifyDate, c.CreationDate, c.OffsetTime, c.OffsetTimeOriginal, c.OffsetTimeDigitized)
			FROM AlbumPhoto a
			INNER JOIN PhotoOwner b ON b.photoId = a.photoId AND b.userId = a.userId
			INNER JOIN PhotoMetadata c ON c.photoId = a.photoId
			INNER JOIN Photo d ON d.id = a.photoId
			WHERE a.albumId = :albumId
		"""
	)
	fun findPhotoDetailByAlbumId(albumId: String): List<ApiPhotoMetadata>

	fun findByUserIdAndPhotoId(userId: String, photoId: String): List<AlbumPhoto>

	fun countByAlbumId(albumId: String): Int

	@Modifying
	@Query("UPDATE AlbumPhoto SET photoId = :to WHERE photoId = :from")
	fun updatePhotoId(from: String, to: String): Int
}
