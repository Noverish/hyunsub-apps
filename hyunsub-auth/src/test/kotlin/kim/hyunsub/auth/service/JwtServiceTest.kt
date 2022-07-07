package kim.hyunsub.auth.service

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import kim.hyunsub.auth.config.JwtProperties
import kim.hyunsub.common.web.model.UserAuth
import java.time.Duration

class JwtServiceTest : FreeSpec({
	val idNo = "kotest_id_no"

	val jwtProperties = JwtProperties(
		key = "PXI5obKzpshsBHGHzKAGoSjRGNUBkpuBTTWLrSa9c98=",
		duration = Duration.ofHours(1),
		iv = "UwLc2X3Z+h+V4jWBdzBdPQ==",
	)
	val service = JwtService(jwtProperties)

	"encryptPayload, decryptPayload" {
		val payload = UserAuth(idNo)

		val encrypted = service.encryptPayload(payload)
		println("encrypted: $encrypted")
		val result = service.decryptPayload(encrypted)

		result shouldBe payload
	}

	"issue, verify" {
		val payload = UserAuth(idNo)

		val jwt = service.issue(payload)
		println("jwt: $jwt")
		val result = service.verify(jwt)

		result shouldBe payload
	}
})
