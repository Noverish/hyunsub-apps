package kim.hyunsub.vestige.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/vestiges")
class VestigeController {
	@GetMapping("")
	fun list(): String {
		return "Hello, World!"
	}
}
