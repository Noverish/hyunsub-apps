package kim.hyunsub.video.model

import com.fasterxml.jackson.annotation.JsonValue

enum class VideoSort(@JsonValue val value: String) {
	RANDOM("random"),
	NEW("new"),
	OLD("old"),
	ABC("abc"),
	ZYX("zyx"),
}
