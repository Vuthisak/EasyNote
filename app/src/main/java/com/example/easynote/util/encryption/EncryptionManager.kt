//package com.example.easynote.util.encryption
//
//import android.content.Context
//
//object EncryptionManager {
//
//    private const val MASTER_KEY = "master_key"
//    const val KEY_PAIR_ALGORITHM = "RSA"
//    const val KEY_PROVIDER = "AndroidKeyStore"
//    private const val TRANSFORMATION_ASYMMETRIC = "RSA/None/PKCS1Padding"
//
//    fun createMasterKey() {
//        if (keyStoreWrapper.getAndroidKeyStoreAsymmetricKeyPair(MASTER_KEY) == null) {
//            keyStoreWrapper.createAndroidKeyStoreAsymmetricKey(MASTER_KEY)
//        }
//    }
//
//    fun encryptData(data: String): String? {
//        val masterKey = keyStoreWrapper.getAndroidKeyStoreAsymmetricKeyPair(MASTER_KEY)
//        return CipherWrapper(TRANSFORMATION_ASYMMETRIC).encrypt(data, masterKey?.public)
//    }
//
//    fun decryptData(data: String): String? {
//        val masterKey = keyStoreWrapper.getAndroidKeyStoreAsymmetricKeyPair(MASTER_KEY)
//        return CipherWrapper(TRANSFORMATION_ASYMMETRIC).decrypt(data, masterKey?.private)
//    }
//
//    fun removeMasterKey() {
//        keyStoreWrapper.removeAndroidKeyStoreKey(MASTER_KEY)
//    }
//
//}
