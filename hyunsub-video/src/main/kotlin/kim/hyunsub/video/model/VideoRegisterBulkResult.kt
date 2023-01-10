package kim.hyunsub.video.model

import kim.hyunsub.video.repository.entity.Video
import kim.hyunsub.video.repository.entity.VideoEntry
import kim.hyunsub.video.repository.entity.VideoMetadata

data class VideoRegisterBulkResult(
	val entry: VideoEntry,
	val videos: List<Video>,
	val metadatas: List<VideoMetadata>,
)
