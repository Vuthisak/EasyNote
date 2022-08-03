package com.example.easynote.util.encryption

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object SecurityUtils {

    private const val TRANSFORMATION = "AES/CBC/PKCS5Padding"
    private const val ALGORITHM = "AES"
    private val cipher: Cipher = Cipher.getInstance(TRANSFORMATION)
    private val aesKey = ByteArray(32)
    private val iV = "71rtygfh23282qw5"
    private val aesIv = ByteArray(16)

    fun encrypt(data: String, key: String): String? {
        return try {
            val keySpec = getSecretKeySpec(key)
            val iV = getInitVectorSpec()
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iV)
            val dataBytes = data.toByteArray(Charsets.UTF_8)
            val results = cipher.doFinal(dataBytes)
            return Base64.encodeToString(results, Base64.NO_WRAP)
        } catch (ex: Exception) {
            null
        }
    }

    fun decrypt(data: String, key: String): String? {
        return try {
            val keySpec = getSecretKeySpec(key)
            val iV = getInitVectorSpec()
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iV)
            val encryptedData = Base64.decode(data.toByteArray(Charsets.UTF_8), Base64.NO_WRAP)
            val result = cipher.doFinal(encryptedData)
            String(result)
        } catch (ex: Exception) {
            data
        }
    }

    private fun getSecretKeySpec(key: String): SecretKeySpec {
        val keyBytes = key.toByteArray()
        var keyLength = keyBytes.size
        if (keyLength > aesKey.size) {
            keyLength = aesKey.size
        }

        System.arraycopy(keyBytes, 0, aesKey, 0, keyLength)
        return SecretKeySpec(aesKey, ALGORITHM)
    }

    private fun getInitVectorSpec(): IvParameterSpec {
        val iv = iV.toByteArray()
        var ivLength = iv.size
        if (ivLength > aesIv.size) {
            ivLength = aesIv.size
        }

        System.arraycopy(iv, 0, aesIv, 0, ivLength)
        return IvParameterSpec(aesIv)
    }

}
