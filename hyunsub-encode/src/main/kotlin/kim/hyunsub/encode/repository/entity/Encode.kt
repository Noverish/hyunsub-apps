package kim.hyunsub.encode.repository.entity

import kim.hyunsub.common.log.hashWithMD5
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table
import kotlin.io.path.Path
import kotlin.io.path.extension

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

	@Column(nullable = false)
	val output: String,

	@Column(nullable = false)
	val progress: Int = 0,

	@Column(nullable = false)
	val regDt: LocalDateTime,

	@Column
	val startDt: LocalDateTime? = null,

	@Column
	val endDt: LocalDateTime? = null,

	@Column
	val callback: String? = null,
) {
	val encodeOutput: String
		get() {
			if (input != output) {
				return output
			}

			val hash = input.hashWithMD5()
			val path = Path(input)
			val ext = path.extension
			val dir = path.parent.toString()
			return Path(dir, "$hash.$ext").toString()
		}
}
