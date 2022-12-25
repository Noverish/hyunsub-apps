package kim.hyunsub.drive.service

import kim.hyunsub.common.web.model.UserAuth
import org.springframework.stereotype.Service
import kotlin.io.path.Path

@Service
class DrivePathService {
	fun getBasePath(userAuth: UserAuth): String {
		return if (userAuth.isAdmin) {
			"/"
		} else {
			"/hyunsub/drive/${userAuth.idNo}"
		}
	}

	fun getPath(userAuth: UserAuth, path: String): String {
		return Path(getBasePath(userAuth), path).toString()
	}
}
