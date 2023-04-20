package kim.hyunsub.photo.repository

import kim.hyunsub.photo.repository.entity.AlbumOwner
import kim.hyunsub.photo.repository.entity.AlbumOwnerId
import org.springframework.data.jpa.repository.JpaRepository

interface AlbumOwnerRepository : JpaRepository<AlbumOwner, AlbumOwnerId> {
	fun countByUserId(userId: String): Int
}
