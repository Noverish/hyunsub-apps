package kim.hyunsub.apparel.service

object FilePathConverter {
	fun getApparelImagePath(userId: String, apparelId: String, fileName: String) =
		"/hyunsub/apparel/apparel/$userId/$apparelId/$fileName"
}
