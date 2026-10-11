package dev.estopia.free_grind.google_oauth

object FreeGrindTrust {
	const val PACKAGE = "dev.estopia.free_grind"

	val SIGNING_CERTS_SHA256 = setOf(
		// Release builds, signed by GitHub Actions (ANDROID_KEYSTORE_BASE64)
		"7CE9E5CDAC620EC798BEA6749AED4C6D59BF12AB5E1DC8CB59C117BB929FC9D0",
		// Local builds, signed by src-tauri/certs/free-grind-dev.jks
		"7EAEC68446F1C2E14D2749807CD63629319C4E59015A16A408A6BD668B7FE48E",
	)

	fun interface SigningCertificates {
		fun has(packageName: String, sha256: ByteArray): Boolean
	}

	fun trusts(
		packageName: String?,
		certificates: SigningCertificates,
	): Boolean = packageName == PACKAGE &&
		SIGNING_CERTS_SHA256.any { cert ->
			certificates.has(
				packageName = PACKAGE,
				sha256 = cert.hexToByteArray(),
			)
		}
}
