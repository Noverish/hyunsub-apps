package kim.hyunsub.diary.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.Table
import java.io.Serializable
import java.time.LocalDate

@Entity
@Table(name = "diary")
@IdClass(DiaryId::class)
data class Diary(
	@Id
	val userId: String,

	@Id
	val date: LocalDate,

	@Column(nullable = false)
	val summary: String,

	@Column(nullable = false)
	val content: String,

	@Column(nullable = false)
	val length: Int = content.length,
)

data class DiaryId(
	val userId: String = "",
	val date: LocalDate = LocalDate.now(),
) : Serializable
