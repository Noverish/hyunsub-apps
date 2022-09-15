package kim.hyunsub.encode.repository.entity

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "encode")
data class Encode(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Int = 0,

	@Column(nullable = false)
	val input: String,

	@Column(nullable = false)
	val options: String,

	@Column
	val output: String? = null,

	@Column(nullable = false)
	val progress: Int = 0,

	@Column(nullable = false)
	val regDt: LocalDateTime,

	@Column
	val startDt: LocalDateTime? = null,

	@Column
	val endDt: LocalDateTime? = null,
)
