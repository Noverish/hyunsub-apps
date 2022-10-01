package kim.hyunsub.apparel.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import kim.hyunsub.apparel.repository.ApparelImageRepository
import kim.hyunsub.apparel.repository.ApparelRepository
import kim.hyunsub.apparel.repository.entity.Apparel
import kim.hyunsub.common.log.Log
import kim.hyunsub.common.random.RandomGenerator
import kim.hyunsub.common.util.convertToMap
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import org.springframework.stereotype.Service

@Service
class ApparelService(
	private val apparelRepository: ApparelRepository,
	private val apparelImageRepository: ApparelImageRepository,
	private val randomGenerator: RandomGenerator,
	private val apparelImageService: ApparelImageService,
	private val mapper: ObjectMapper,
) {
	companion object : Log

	fun create(userId: String, body: Map<String, Any?>): Apparel {
		val id = Apparel.generateId(randomGenerator)

		val map = buildMap {
			this += body.mapValues { if (it.value == "") null else it.value }
			this["id"] = id
			this["userId"] = userId
		}

		log.debug("[Apparel Create] map={}", map)
		val apparel = mapper.convertValue<Apparel>(map)
		apparelRepository.save(apparel)
		log.debug("[Create Apparel] apparel={}", apparel)

		return apparel
	}

	fun update(userId: String, apparelId: String, params: Map<String, Any?>): Apparel {
		log.debug("[Apparel Update] userId={}, apparelId={}, params={}", userId, apparelId, params)

		val apparel = apparelRepository.findByIdAndUserId(apparelId, userId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)
		log.debug("[Apparel Update] apparel={}", apparel)

		val map = mapper.convertToMap(apparel) + params
		log.debug("[Apparel Update] map={}", map)

		val newApparel = mapper.convertValue<Apparel>(map)
		log.debug("[Apparel Update] newApparel={}", newApparel)

		apparelRepository.save(newApparel)
		return newApparel
	}

	fun delete(userId: String, apparelId: String) {
		log.debug("[Apparel Delete] userId={}, apparelId={}", userId, apparelId)

		val apparel = apparelRepository.findByIdAndUserId(apparelId, userId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)
		log.debug("[Apparel Delete] apparel={}", apparel)

		val images = apparelImageRepository.findByApparelId(apparelId)
		log.debug("[Apparel Delete] images={}", images)

		images.forEach {
			apparelImageRepository.delete(it)
			apparelImageService.deleteFile(userId, it)
		}

		apparelRepository.delete(apparel)
	}
}
