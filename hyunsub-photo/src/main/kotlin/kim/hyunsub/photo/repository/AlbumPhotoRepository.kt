package kim.hyunsub.photo.repository

import kim.hyunsub.photo.model.api.RestApiPhotoMetadata
import kim.hyunsub.photo.repository.entity.AlbumPhoto
import kim.hyunsub.photo.repository.entity.AlbumPhotoId
import kim.hyunsub.photo.repository.entity.PhotoV2
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface AlbumPhotoRepository : JpaRepository<AlbumPhoto, AlbumPhotoId> {
	@Query(
		"""
			SELECT b FROM AlbumPhoto a
			INNER JOIN PhotoV2 b ON b.id = a.photoId
			WHERE a.albumId = :albumId
		"""
	)
	fun findByAlbumId(albumId: String): List<PhotoV2>

	@Query(
		"""
			SELECT new kim.hyunsub.photo.model.api.RestApiPhotoMetadata(a.photoId, d.offset, a.userId, b.name, b.fileDt, c.SubSecDateTimeOriginal, c.DateTimeOriginal, c.GPSDateTime, c.TimeStamp, c.ModifyDate, c.CreationDate, c.OffsetTime, c.OffsetTimeOriginal, c.OffsetTimeDigitized)
			FROM AlbumPhoto a
			INNER JOIN PhotoOwner b ON b.photoId = a.photoId AND b.userId = a.userId
			INNER JOIN PhotoMetadataV2 c ON c.photoId = a.photoId
			INNER JOIN PhotoV2 d ON d.id = a.photoId
			WHERE a.albumId = :albumId
		"""
	)
	fun findPhotoDetailByAlbumId(albumId: String): List<RestApiPhotoMetadata>

	fun findByUserIdAndPhotoId(userId: String, photoId: String): List<AlbumPhoto>

	fun countByAlbumId(albumId: String): Int

	@Modifying
	@Query("UPDATE AlbumPhoto SET photoId = :to WHERE photoId = :from")
	fun updatePhotoId(from: String, to: String): Int
}
