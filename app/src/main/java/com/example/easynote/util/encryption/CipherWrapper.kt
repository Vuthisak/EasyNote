package com.example.easynote.util.encryption

import android.util.Base64
import java.security.Key
import javax.crypto.Cipher

class CipherWrapper(private val transformation: String) {

    private val cipher: Cipher = Cipher.getInstance(transformation)

    fun encrypt(data: String, key: Key?): String? {
        return try {
            cipher.init(Cipher.ENCRYPT_MODE, key)
            val dataBytes = data.toByteArray(Charsets.UTF_8)
            val results = cipher.doFinal(dataBytes)
            return Base64.encodeToString(results, Base64.NO_WRAP)
        } catch (ex: Exception) {
            null
        }
    }

    fun decrypt(data: String, key: Key?): String? {
        return try {
            val cipher: Cipher = Cipher.getInstance(transformation)
            cipher.init(Cipher.DECRYPT_MODE, key)
            val encryptedData = Base64.decode(data.toByteArray(Charsets.UTF_8), Base64.NO_WRAP)
            val result = cipher.doFinal(encryptedData)
            String(result)
        } catch (ex: Exception) {
            null
        }
    }

}
