package kim.hyunsub.common.web.annotation

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Authorized(
	/**
	 * List of authorities for access API
	 */
	val authorities: Array<String> = [],
)
