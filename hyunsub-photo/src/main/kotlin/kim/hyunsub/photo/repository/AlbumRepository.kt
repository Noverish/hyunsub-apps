package kim.hyunsub.photo.repository

import kim.hyunsub.photo.repository.entity.Album
import org.springframework.data.jpa.repository.JpaRepository

interface AlbumRepository : JpaRepository<Album, Int>
