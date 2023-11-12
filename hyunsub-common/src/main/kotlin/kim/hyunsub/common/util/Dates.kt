import java.time.LocalDateTime
import java.time.ZoneId

fun LocalDateTime.toEpochMillis() =
	this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
