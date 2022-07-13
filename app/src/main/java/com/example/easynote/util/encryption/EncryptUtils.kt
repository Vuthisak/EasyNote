package com.example.easynote.util.encryption

import android.util.Base64

object EncryptUtils {

    fun encodeBase64(text: String): String {
        return Base64.encode(text.toByteArray(), Base64.NO_WRAP).toString()
    }

    fun decodeBase64(encodedText: String): String{
        return Base64.decode(encodedText, Base64.NO_WRAP).toString()
    }

}
