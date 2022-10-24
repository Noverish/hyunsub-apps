package kim.hyunsub.division.repository.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "division_gathering")
data class Gathering(
	@Id
	@Column(columnDefinition = "CHAR(6)")
	val id: String,

	@Column(nullable = false)
	val name: String,
)
