package kim.hyunsub.diary.repository.entity

import org.hibernate.annotations.Type
import java.io.Serializable
import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.Table

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

	@Type(type = "text")
	@Column(nullable = false)
	val content: String,

	@Column(nullable = false)
	val length: Int = content.length,
)

data class DiaryId(
	val userId: String = "",
	val date: LocalDate = LocalDate.now(),
) : Serializable
