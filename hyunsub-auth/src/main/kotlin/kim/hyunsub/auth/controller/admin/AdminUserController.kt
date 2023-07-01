package kim.hyunsub.auth.controller.admin

import kim.hyunsub.auth.model.dto.UserCreateParams
import kim.hyunsub.auth.model.toApi
import kim.hyunsub.auth.model.user.ApiUser
import kim.hyunsub.auth.service.UserService
import kim.hyunsub.common.web.annotation.Authorized
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Authorized(["admin"])
@RestController
@RequestMapping("/api/v1/admin/users")
class AdminUserController(
	private val userService: UserService,
) {
	@GetMapping("")
	fun list(): List<ApiUser> =
		userService.list().map { it.toApi() }

	@PostMapping("")
	fun create(@RequestBody params: UserCreateParams): ApiUser =
		userService.create(params).toApi()

	@GetMapping("/{idNo}")
	fun get(@PathVariable idNo: String): ApiUser =
		userService.get(idNo).toApi()

	@DeleteMapping("/{idNo}")
	fun delete(@PathVariable idNo: String): ApiUser =
		userService.delete(idNo).toApi()
}
