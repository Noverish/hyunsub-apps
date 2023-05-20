package kim.hyunsub.apparel.service

object ApparelPathConverter {
	fun imagePath(userId: String, apparelId: String, fileName: String) =
		"/hyunsub/apparel/apparel/$userId/$apparelId/$fileName"
}
