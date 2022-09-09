package kim.hyunsub.video.service

import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import kim.hyunsub.common.api.ApiCaller
import kim.hyunsub.common.log.Log
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.video.repository.VideoMetadataRepository
import kim.hyunsub.video.repository.VideoRepository
import kim.hyunsub.video.repository.entity.VideoMetadata
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class VideoMetadataService(
	private val apiCaller: ApiCaller,
	private val videoRepository: VideoRepository,
	private val videoMetadataRepository: VideoMetadataRepository,
) {
	companion object : Log

	fun scanAndSave(videoId: String): VideoMetadata {
		log.debug("scanAndSave: videoId=$videoId")
		val video = videoRepository.findByIdOrNull(videoId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)
		log.debug("scanAndSave: video=$video")

		val ffprobe = apiCaller.ffprobe(video.path)
		log.debug("scanAndSave: ffprobe=$ffprobe")

		val streams = ffprobe["streams"] as ArrayNode
		val format = ffprobe["format"] as ObjectNode
		val videoStream = streams.first { it["codec_type"].asText() == "video" }
		val audioStream = streams.first { it["codec_type"].asText() == "audio" }

		val videoMetadata = VideoMetadata(
			path = video.path,
			nbStreams = format["nb_streams"].asInt(),
			duration = format["duration"].asInt(),
			size = format["size"].asLong(),
			bitrate = format["bit_rate"].asInt(),
			width = videoStream["width"].asInt(),
			height = videoStream["height"].asInt(),
			videoCodecName = videoStream["codec_name"].asText(),
			videoProfile = videoStream["profile"]?.asText() ?: "",
			videoPixFmt = videoStream["pix_fmt"].asText(),
			videoLevel = videoStream["level"].asInt(),
			videoBitrate = videoStream["bit_rate"]?.asInt() ?: 0,
			audioCodecName = audioStream["codec_name"].asText(),
			audioProfile = audioStream["profile"]?.asText() ?: "",
			audioSampleRate = audioStream["sample_rate"].asInt(),
			audioChannelLayout = (audioStream["channel_layout"] ?: audioStream["channels"]).asText(),
			audioBitrate = audioStream["bit_rate"]?.asInt() ?: 0,
			date = LocalDateTime.now(),
		)
		log.debug("scanAndSave: videoMetadata=$videoMetadata")

		videoMetadataRepository.save(videoMetadata)

		return videoMetadata
	}
}
