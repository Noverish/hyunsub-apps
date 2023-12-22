package kim.hyunsub.common.kms

import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.regions.Regions
import com.amazonaws.services.kms.AWSKMSClientBuilder
import com.amazonaws.services.kms.model.DecryptRequest
import com.amazonaws.services.kms.model.EncryptRequest
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.util.Base64

object KmsEncryptor {
	fun encrypt(profile: String, keyId: String, text: String): String {
		val kmsClient = AWSKMSClientBuilder.standard()
			.withCredentials(ProfileCredentialsProvider(profile))
			.withRegion(Regions.AP_NORTHEAST_2)
			.build()

		val request = EncryptRequest()
		request.withKeyId(keyId)
		request.withPlaintext(ByteBuffer.wrap(text.toByteArray(StandardCharsets.UTF_8)))

		val cipherBytes = kmsClient.encrypt(request).ciphertextBlob.array()
		return Base64.getEncoder().encodeToString(cipherBytes)
	}

	fun decrypt(profile: String, keyId: String, cipher: String): String {
		val kmsClient = AWSKMSClientBuilder.standard()
			.withCredentials(ProfileCredentialsProvider(profile))
			.withRegion(Regions.AP_NORTHEAST_2)
			.build()

		val request = DecryptRequest()
		request.withKeyId(keyId)
		request.withCiphertextBlob(ByteBuffer.wrap(Base64.getDecoder().decode(cipher)))

		val textBytes = kmsClient.decrypt(request).plaintext.array()
		return String(textBytes)
	}
}
