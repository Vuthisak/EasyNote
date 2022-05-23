package com.example.easynote.repository

import com.example.easynote.entity.Note
import com.example.easynote.util.await
import com.example.easynote.util.getOrDefault
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

interface NoteRepository {
    suspend fun saveNote(note: Note): Flow<Note?>
    suspend fun updateNote(note: Note): Flow<Void>
    suspend fun getNotes(): Flow<MutableList<Note>>
    suspend fun removeNote(noteId: String): Flow<Void>
}

class NoteRepositoryImpl : NoteRepository {

    private val firestore: FirebaseFirestore = Firebase.firestore

    override suspend fun updateNote(note: Note): Flow<Void> = withContext(Dispatchers.IO) {
        flow {
            val result = firestore
                .collection(COLLECTION)
                .document(note.id.getOrDefault())
                .set(note)
                .await()
            emit(result)
        }
    }

    override suspend fun removeNote(noteId: String): Flow<Void> = withContext(Dispatchers.IO) {
        flow {
            val result = firestore
                .collection(COLLECTION)
                .document(noteId)
                .delete()
                .await()
            emit(result)
        }
    }

    override suspend fun saveNote(note: Note): Flow<Note?> = withContext(Dispatchers.IO) {
        flow {
            val result = firestore
                .collection(COLLECTION)
                .add(note)
                .await()
                .get()
                .await()
                .toObject(Note::class.java)
            emit(result)
        }
    }

    override suspend fun getNotes(): Flow<MutableList<Note>> = withContext(Dispatchers.IO) {
        flow {
            val items = mutableListOf<Note>()
            firestore
                .collection(COLLECTION)
                .get()
                .await()
                .documents.forEach {
                    it.toObject(Note::class.java)?.let { note ->
                        note.id = it.id
                        items.add(note)
                    }
                }
            emit(items)
        }
    }

    private companion object {
        const val COLLECTION = "notes"
    }

}
