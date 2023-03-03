package kim.hyunsub.drive.model

import kotlin.io.path.Path
import kotlin.io.path.extension

enum class FileType {
	FOLDER,
	IMAGE,
	VIDEO,
	AUDIO,
	TEXT,
	PDF,
	ETC;

	companion object {
		fun fromPath(path: String): FileType {
			return when (Path(path).extension.lowercase()) {
				"jpg", "jpeg", "png", "gif" -> IMAGE
				"mp4" -> VIDEO
				"mp3", "aac" -> AUDIO
				"pdf" -> PDF
				"txt", "md", "srt", "smi", "sh", "yml", "json" -> TEXT
				else -> ETC
			}
		}
	}
}
