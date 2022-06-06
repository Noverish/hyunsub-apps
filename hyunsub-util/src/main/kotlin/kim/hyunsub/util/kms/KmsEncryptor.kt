package kim.hyunsub.util.kms

import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.regions.Regions
import com.amazonaws.services.kms.AWSKMSClientBuilder
import com.amazonaws.services.kms.model.DecryptRequest
import com.amazonaws.services.kms.model.EncryptRequest
import org.springframework.util.Base64Utils
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

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
		return Base64Utils.encodeToString(cipherBytes)
	}

	fun decrypt(profile: String, keyId: String, cipher: String): String {
		val kmsClient = AWSKMSClientBuilder.standard()
			.withCredentials(ProfileCredentialsProvider(profile))
			.withRegion(Regions.AP_NORTHEAST_2)
			.build()

		val request = DecryptRequest()
		request.withKeyId(keyId)
		request.withCiphertextBlob(ByteBuffer.wrap(Base64Utils.decodeFromString(cipher)))

		val textBytes = kmsClient.decrypt(request).plaintext.array()
		return String(textBytes)
	}
}
