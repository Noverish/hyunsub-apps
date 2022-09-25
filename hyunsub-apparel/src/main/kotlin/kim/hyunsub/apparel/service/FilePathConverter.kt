package kim.hyunsub.apparel.service

object FilePathConverter {
	fun getApparelPhotoPath(userId: String, apparelId: String, fileName: String) =
		"/hyunsub/apparel/apparel/$userId/$apparelId/$fileName"
}
