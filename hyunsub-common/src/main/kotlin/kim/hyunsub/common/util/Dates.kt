package kim.hyunsub.common.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId

fun LocalDateTime.toMillis(): Long =
	this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

fun OffsetDateTime.toMillis(): Long =
	this.toInstant().toEpochMilli()

fun Long.toLdt(): LocalDateTime =
	Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDateTime()

fun Long.toOdt(zoneId: ZoneId = ZoneId.systemDefault()): OffsetDateTime =
	Instant.ofEpochMilli(this).atZone(zoneId).toOffsetDateTime()

fun LocalDateTime.toOdt(zoneId: ZoneId = ZoneId.systemDefault()): OffsetDateTime =
	this.atZone(zoneId).toOffsetDateTime()
