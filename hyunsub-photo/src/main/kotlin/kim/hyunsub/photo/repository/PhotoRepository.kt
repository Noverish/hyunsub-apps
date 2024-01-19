package kim.hyunsub.photo.repository

import kim.hyunsub.common.util.toMillis
import kim.hyunsub.photo.repository.entity.Photo
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.time.OffsetDateTime

interface PhotoRepository : JpaRepository<Photo, String> {
	@Query(
		"""
			SELECT b FROM PhotoOwner a
			INNER JOIN Photo b ON b.id = a.photoId
			WHERE a.userId = :userId
			ORDER BY a.photoId DESC
		"""
	)
	fun selectMyPhotos(userId: String, page: Pageable = Pageable.unpaged()): List<Photo>

	@Query(
		"""
			SELECT b FROM PhotoOwner a
			INNER JOIN Photo b ON b.id = a.photoId
			WHERE a.userId = :userId AND a.photoId < :photoId
			ORDER BY a.photoId DESC
		"""
	)
	fun selectMyPhotosWithNext(userId: String, photoId: String, page: Pageable): List<Photo>

	@Query(
		"""
			SELECT b FROM PhotoOwner a
			INNER JOIN Photo b ON b.id = a.photoId
			WHERE a.userId = :userId AND a.photoId > :photoId
			ORDER BY a.photoId ASC
		"""
	)
	fun selectMyPhotosWithPrev(userId: String, photoId: String, page: Pageable): List<Photo>

	@Query(
		"""
			SELECT b FROM PhotoOwner a
			INNER JOIN Photo b ON b.id = a.photoId
			WHERE a.userId = :userId AND a.photoId = :photoId
			ORDER BY a.photoId DESC
		"""
	)
	fun selectMyPhotosWithPhotoId(userId: String, photoId: String): Photo?

	fun findByHash(hash: String): Photo?

	@Modifying
	@Query("UPDATE Photo SET id = :to where id = :from")
	fun updateId(from: String, to: String): Int
}

fun PhotoRepository.generateId(date: OffsetDateTime, hash: String): String {
	for (i in 0 until 3) {
		val id = Photo.generateId(date.toMillis(), hash, i)
		if (!this.existsById(id)) {
			return id
		}
	}
	throw RuntimeException("Failed to generate new id")
}

/*
@Repository
class PhotoSearchDao {
	@PersistenceContext
	private lateinit var entityManager: EntityManager

	fun search(userId: String, query: ApiPhotoSearchQuery): RestApiPagination<Photo> {
		val cb = entityManager.criteriaBuilder

		fun buildQuery(cq: CriteriaQuery<*>): Root<Photo> {
			val root = cq.from(Photo::class.java)

			val predicates = buildList<Predicate> {
				add(cb.equal(root.get<String>("userId"), userId))
				add(
					cb.or(
						cb.like(root.get("content"), "%$query%"),
						cb.like(root.get("summary"), "%$query%"),
					)
				)

				query.prev?.let {
					add(cb.greaterThan(root.get("date"), it))
				}

				query.next?.let {
					add(cb.lessThan(root.get("date"), it))
				}
			}

			cq.where(cb.and(*predicates.toTypedArray()))
			cq.orderBy(cb.desc(root.get<LocalDate>("date")))

			return root
		}

		val selectQuery = cb.createQuery(Photo::class.java)
		val selectRoot = buildQuery(selectQuery)
		selectQuery.select(selectRoot)
		val list = entityManager.createQuery(selectQuery)
			.setMaxResults(query.pageSize)
			.resultList

		val countQuery = cb.createQuery(Long::class.java)
		val countRoot = buildQuery(countQuery)
		countQuery.select(cb.count(countRoot))
		val count = entityManager.createQuery(countQuery)
			.singleResult

		return RestApiPagination(
			total = count.toInt(),
			prev = list.firstOrNull()?.date?.let { localDateFormatter.format(it) },
			next = list.lastOrNull()?.date?.let { localDateFormatter.format(it) },
			data = list,
		)
	}
}
 */
