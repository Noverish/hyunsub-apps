package kim.hyunsub.drive.controller

import kim.hyunsub.common.config.AppConstants
import kim.hyunsub.common.fs.client.FsClient
import kim.hyunsub.common.fs.client.remove
import kim.hyunsub.common.fs.model.FsRsyncParams
import kim.hyunsub.common.fs.model.UserDeleteParams
import kim.hyunsub.common.fs.model.UserInitParams
import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.model.SimpleResponse2
import mu.KotlinLogging
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Authorized(["server"])
@RestController
@RequestMapping("/api/v1/server")
class DriveServerController(
	private val fsClient: FsClient,
) {
	private val log = KotlinLogging.logger { }

	@PostMapping("/user/init")
	fun userInit(@RequestBody params: UserInitParams): SimpleResponse2 {
		log.debug { "[User Init] params=$params" }

		if (!params.dryRun) {
			fsClient.rsync(
				FsRsyncParams(
					from = "/hyunsub/drive/${AppConstants.INIT_FROM_USER_ID}/",
					to = "/hyunsub/drive/${params.userId}",
				)
			)
		}

		return SimpleResponse2()
	}

	@PostMapping("/user/delete")
	fun userDelete(@RequestBody params: UserDeleteParams): SimpleResponse2 {
		log.debug { "[User Delete] params=$params" }

		fsClient.remove("/hyunsub/drive/${params.userId}")

		return SimpleResponse2()
	}
}
