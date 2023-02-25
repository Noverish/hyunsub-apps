package kim.hyunsub.video.service.scan

import kim.hyunsub.video.model.VideoScanResult

interface VideoScanner {
	fun scan(path: String): VideoScanResult
}
