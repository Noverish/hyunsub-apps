package kim.hyunsub.drive.service

import kim.hyunsub.common.web.model.UserAuth
import org.springframework.stereotype.Service

@Service
class DrivePathService {
	fun getBasePath(userAuth: UserAuth): String {
		return if (userAuth.isAdmin) {
			"/"
		} else {
			"/hyunsub/drive/${userAuth.idNo}"
		}
	}
}
