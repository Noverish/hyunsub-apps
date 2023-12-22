package kim.hyunsub.apparel.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "apparel_preview")
data class ApparelPreview(
	@Id
	@Column(length = 8)
	val id: String,

	@Column(nullable = false)
	val userId: String,

	@Column(nullable = false)
	val name: String,

	@Column(nullable = false)
	val category: String,

	@Column
	val brand: String?,

	@Column(length = 10)
	val imageId: String?,

	@Column
	val ext: String?,
) {
	val fileName: String?
		get() = if (imageId != null && ext != null) "$imageId.$ext" else null
}
