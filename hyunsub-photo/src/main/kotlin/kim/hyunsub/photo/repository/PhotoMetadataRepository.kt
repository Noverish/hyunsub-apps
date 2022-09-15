package kim.hyunsub.photo.repository

import kim.hyunsub.photo.repository.entity.PhotoMetadata
import org.springframework.data.jpa.repository.JpaRepository

interface PhotoMetadataRepository : JpaRepository<PhotoMetadata, String>
