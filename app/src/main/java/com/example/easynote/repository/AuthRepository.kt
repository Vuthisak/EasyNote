package com.example.easynote.repository

import com.example.easynote.util.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

interface AuthRepository {
    suspend fun register(email: String, password: String): Flow<FirebaseUser?>
    suspend fun login(email: String, password: String): Flow<FirebaseUser?>
    suspend fun requestResetPassword(email: String): Flow<Void>
    suspend fun confirmResetPassword(code: String, newPassword: String): Flow<Void>
}

class AuthRepositoryImpl : AuthRepository {

    private val firebaseAuth = FirebaseAuth.getInstance()

    override suspend fun register(email: String, password: String): Flow<FirebaseUser?> = flow {
        val result = firebaseAuth
            .createUserWithEmailAndPassword(email, password)
            .await()
        emit(result.user)
    }.flowOn(Dispatchers.IO)

    override suspend fun login(email: String, password: String): Flow<FirebaseUser?> = flow {
        val result = firebaseAuth
            .signInWithEmailAndPassword(email, password)
            .await()
        emit(result.user)
    }.flowOn(Dispatchers.IO)

    override suspend fun requestResetPassword(email: String): Flow<Void> = flow<Void> {
        val result = firebaseAuth
            .sendPasswordResetEmail(email)
            .await()
        emit(result)
    }.flowOn(Dispatchers.IO)

    override suspend fun confirmResetPassword(
        code: String, newPassword: String
    ): Flow<Void> = flow<Void> {
        val result = firebaseAuth
            .confirmPasswordReset(code, newPassword)
            .await()
        emit(result)
    }.flowOn(Dispatchers.IO)

}
