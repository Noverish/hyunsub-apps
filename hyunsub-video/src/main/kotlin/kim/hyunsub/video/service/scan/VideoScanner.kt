package kim.hyunsub.video.service.scan

import kim.hyunsub.video.model.ScanResult

interface VideoScanner {
	fun scan(path: String): ScanResult
}
