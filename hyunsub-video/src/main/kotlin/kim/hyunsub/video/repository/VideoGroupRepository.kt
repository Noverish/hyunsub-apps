package kim.hyunsub.video.repository

import kim.hyunsub.video.repository.entity.VideoGroup
import org.springframework.data.jpa.repository.JpaRepository

interface VideoGroupRepository: JpaRepository<VideoGroup, String>
