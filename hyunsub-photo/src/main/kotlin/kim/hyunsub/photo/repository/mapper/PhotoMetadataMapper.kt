package kim.hyunsub.photo.repository.mapper

import kim.hyunsub.photo.repository.entity.ApiPhotoMetadata
import kim.hyunsub.photo.repository.entity.PhotoMetadata
import org.apache.ibatis.annotations.Mapper

/**
 * PK: photo_id
 */
@Mapper
interface PhotoMetadataMapper {
	fun selectOne(photoId: String): PhotoMetadata?
	fun selectDetailByAlbumId(albumId: String): List<ApiPhotoMetadata>

	fun upsert(entity: PhotoMetadata)
	fun updatePhotoId(from: String, to: String): Int
}
