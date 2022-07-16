package kim.hyunsub.video.repository

import kim.hyunsub.video.repository.entity.VideoEntry
import org.springframework.data.jpa.repository.JpaRepository

interface VideoEntryRepository: JpaRepository<VideoEntry, String>
