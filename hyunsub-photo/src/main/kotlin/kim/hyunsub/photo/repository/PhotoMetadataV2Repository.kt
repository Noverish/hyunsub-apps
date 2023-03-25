package kim.hyunsub.photo.repository

import kim.hyunsub.photo.repository.entity.PhotoMetadataV2
import org.springframework.data.jpa.repository.JpaRepository

interface PhotoMetadataV2Repository : JpaRepository<PhotoMetadataV2, String>
