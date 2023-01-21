package kim.hyunsub.comic.repository

import kim.hyunsub.comic.repository.entity.Comic
import org.springframework.data.jpa.repository.JpaRepository

interface ComicRepository : JpaRepository<Comic, String>
