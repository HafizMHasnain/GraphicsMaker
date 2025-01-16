import java.security.MessageDigest
import java.util.Properties
import java.io.File

// Function to generate MD5 hash from a string
fun String.toMd5(): String {
    val md = MessageDigest.getInstance("MD5")
    return md.digest(this.toByteArray())
        .joinToString("") { "%02x".format(it) }
}

// Function to load the license key from the properties file
fun loadLicenseKey(): String {
    val licenseFile = File("license.properties")
    if (!licenseFile.exists()) {
        throw GradleException("The 'license.properties' file is missing! Please provide the file to continue.")
    }

    val properties = Properties()
    licenseFile.inputStream().use { properties.load(it) }

    val licenseKey = properties.getProperty("LICENSE_KEY")
    if (licenseKey.isNullOrBlank()) {
        throw GradleException("LICENSE_KEY is missing or empty in 'license.properties'. Please specify a valid key.")
    }

    return licenseKey
}

// Function to validate the license key
fun validateLicenseKey() {
    val licenseKey = loadLicenseKey()

    // Predefined MD5 hash of the valid license key
    val validLicenseHash = "c82c66798cda13f103c1662119255f97"

    if (licenseKey.toMd5() != validLicenseHash) {
        throw GradleException("Invalid LICENSE_KEY provided in 'license.properties'. Please use a valid key.")
    }
    println("License key is valid. Build can proceed.")
}

// Execute validation during the configuration phase
validateLicenseKey()
