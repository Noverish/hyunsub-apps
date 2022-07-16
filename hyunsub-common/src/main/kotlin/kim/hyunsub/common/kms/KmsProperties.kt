package kim.hyunsub.common.kms

// TODO make properties to `val`
data class KmsProperties(
	var profile: String = "",
	var keyId: String = "",
	var properties: Map<String, String> = mutableMapOf(),
)
