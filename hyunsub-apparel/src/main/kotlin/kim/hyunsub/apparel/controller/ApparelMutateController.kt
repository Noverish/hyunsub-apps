package kim.hyunsub.apparel.controller

import kim.hyunsub.apparel.bo.ApparelMutateBo
import kim.hyunsub.apparel.model.ApiApparel
import kim.hyunsub.apparel.model.dto.ApparelCreateParams
import kim.hyunsub.apparel.model.dto.ApparelUpdateParams
import kim.hyunsub.common.web.model.UserAuth
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/apparels")
class ApparelMutateController(
	private val apparelMutateBo: ApparelMutateBo,
) {
	@PostMapping("")
	fun create(
		userAuth: UserAuth,
		@RequestBody params: ApparelCreateParams,
	): ApiApparel {
		return apparelMutateBo.create(userAuth.idNo, params)
	}

	@PutMapping("/{apparelId}")
	fun update(
		userAuth: UserAuth,
		@PathVariable apparelId: String,
		@RequestBody params: ApparelUpdateParams,
	): ApiApparel {
		return apparelMutateBo.update(userAuth.idNo, apparelId, params)
	}

	@DeleteMapping("/{apparelId}")
	fun delete(
		userAuth: UserAuth,
		@PathVariable apparelId: String,
	): ApiApparel {
		return apparelMutateBo.delete(userAuth.idNo, apparelId)
	}
}
