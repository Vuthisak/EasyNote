package com.example.easynote.repository

import com.example.easynote.entity.Note
import com.example.easynote.util.await
import com.example.easynote.util.encryption.EncryptionManager
import com.example.easynote.util.getOrDefault
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

interface NoteRepository {
    suspend fun saveNote(note: Note): Flow<Note?>
    suspend fun updateNote(note: Note): Flow<Void>
    suspend fun getNotes(): Flow<MutableList<Note>>
    suspend fun removeNote(noteId: String): Flow<Void>
}

class NoteRepositoryImpl : NoteRepository {

    private val firestore: FirebaseFirestore = Firebase.firestore
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    override suspend fun updateNote(note: Note): Flow<Void> = flow {
        note.apply {
            title = EncryptionManager.encryptData(note.title.getOrDefault())
            desc = EncryptionManager.encryptData(note.desc.getOrDefault())
        }
        val result = firestore
            .collection(COLLECTION)
            .document(note.id.getOrDefault())
            .set(note)
            .await()
        emit(result)
    }.flowOn(Dispatchers.IO)

    override suspend fun removeNote(noteId: String): Flow<Void> = flow {
        val result = firestore
            .collection(COLLECTION)
            .document(noteId)
            .delete()
            .await()
        emit(result)
    }.flowOn(Dispatchers.IO)

    override suspend fun saveNote(note: Note): Flow<Note?> = flow {
        note.apply {
            userId = this@NoteRepositoryImpl.userId
            title = EncryptionManager.encryptData(note.title.getOrDefault())
            desc = EncryptionManager.encryptData(note.desc.getOrDefault())
        }
        val result = firestore
            .collection(COLLECTION)
            .add(note)
            .await()
            .get()
            .await()
            .toObject(Note::class.java)
        emit(result)
    }.flowOn(Dispatchers.IO)

    override suspend fun getNotes(): Flow<MutableList<Note>> = flow {
        val items = mutableListOf<Note>()
        firestore
            .collection(COLLECTION)
            .whereEqualTo(Note.KEY_USER_ID, userId)
            .get()
            .await()
            .documents.forEach {
                it.toObject(Note::class.java)?.let { note ->
                    note.id = it.id
                    note.title = EncryptionManager.decryptData(note.title.getOrDefault())
                    note.desc = EncryptionManager.decryptData(note.desc.getOrDefault())
                    items.add(note)
                }
            }
        emit(items)
    }.flowOn(Dispatchers.IO)

    private companion object {
        const val COLLECTION = "notes"
    }

}
