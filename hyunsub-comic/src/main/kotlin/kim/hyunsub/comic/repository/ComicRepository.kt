package kim.hyunsub.comic.repository

import kim.hyunsub.comic.repository.entity.Comic
import kim.hyunsub.common.util.generateId
import org.springframework.data.jpa.repository.JpaRepository

interface ComicRepository : JpaRepository<Comic, String>

fun ComicRepository.generateId() = generateId(6)
