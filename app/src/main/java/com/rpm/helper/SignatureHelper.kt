package com.rpm.helper

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.util.Base64
import android.util.Log
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

class SignatureHelper(context: Context) : ContextWrapper(context) {

	fun getAppSignatures() : ArrayList<String> {
		val appCodes = ArrayList<String>()

		try {
			val signatures = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES).signatures
			for (signature: Signature in signatures) {
				val hash = hash(packageName, signature.toCharsString())
				if (hash != null) {
					appCodes.add(String.format("%s", hash))
				}
			}
		} catch (e: PackageManager.NameNotFoundException) {
			Log.e(TAG, "Unable to find package to obtain hash.", e)
		}

		return appCodes
	}

	@SuppressLint("NewApi")
	private fun hash(packageName: String, signature: String): String? {
		val appInfo = "$packageName $signature"
		try {
			val messageDigest = MessageDigest.getInstance(HASH_TYPE)
			messageDigest.update(appInfo.toByteArray(StandardCharsets.UTF_8))
			var hashSignature = messageDigest.digest()

			// truncated into NUM_HASHED_BYTES
			hashSignature = Arrays.copyOfRange(hashSignature, 0, NUM_HASHED_BYTES)
			// encode into Base64
			var base64Hash = Base64.encodeToString(hashSignature, Base64.NO_PADDING or Base64.NO_WRAP)
			base64Hash = base64Hash.substring(0, NUM_BASE64_CHAR)

			Log.e(TAG, String.format("pkg: %s -- hash: %s", packageName, base64Hash))
			return base64Hash
		} catch (e: NoSuchAlgorithmException) {
			Log.e(TAG, "hash:NoSuchAlgorithm", e)
		}

		return null
	}

	companion object {
		private const val TAG = "SignatureHelper"
		private const val HASH_TYPE = "SHA-256"
		private const val NUM_HASHED_BYTES = 9
		private const val NUM_BASE64_CHAR = 11
	}
}
