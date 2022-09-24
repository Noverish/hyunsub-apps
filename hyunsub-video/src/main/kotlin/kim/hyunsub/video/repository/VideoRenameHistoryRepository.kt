package kim.hyunsub.video.repository

import kim.hyunsub.video.repository.entity.VideoRenameHistory
import org.springframework.data.jpa.repository.JpaRepository

interface VideoRenameHistoryRepository : JpaRepository<VideoRenameHistory, Int>
