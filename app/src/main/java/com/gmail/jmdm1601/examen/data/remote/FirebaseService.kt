package com.gmail.jmdm1601.examen.data.remote

import android.net.Uri
import com.gmail.jmdm1601.examen.core.Constants
import com.gmail.jmdm1601.examen.core.Resource
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.*

object FirebaseService {

    suspend fun uploadFile(file: Uri): Resource<Unit> {
        val storage = FirebaseStorage.getInstance()
        val db = FirebaseFirestore.getInstance()
        val storageRef = storage.reference.child(Date().time.toString())

        try {
            val url = storageRef
                .putFile(file)
                .await()
                .storage
                .downloadUrl
                .await()
                .toString()

            db.collection(Constants.IMAGES_COLLECTION)
                .add(mapOf(Constants.FIELD_URL to url)).await()
        } catch (e: Exception) {
            e.printStackTrace()
            return Resource.Failure(e)
        }

        return Resource.Success(Unit)
    }

    suspend fun uploadBytes(bytes: ByteArray): Resource<Unit> {
        val storage = FirebaseStorage.getInstance()
        val db = FirebaseFirestore.getInstance()
        val storageRef = storage.reference.child(Date().time.toString())

        try {
            val url = storageRef
                .putBytes(bytes)
                .await()
                .storage
                .downloadUrl
                .await()
                .toString()

            db.collection(Constants.IMAGES_COLLECTION)
                .add(mapOf(Constants.FIELD_URL to url)).await()
        } catch (e: Exception) {
            e.printStackTrace()
            return Resource.Failure(e)
        }

        return Resource.Success(Unit)
    }

    @ExperimentalCoroutinesApi
    fun getImages(): Flow<List<String>?> {
        val db = FirebaseFirestore.getInstance()
        return callbackFlow {
            val listenerRegistration = db.collection(Constants.IMAGES_COLLECTION)
                .addSnapshotListener { querySnapshot: QuerySnapshot?, firebaseFirestoreException: FirebaseFirestoreException? ->
                    if (firebaseFirestoreException != null) {
                        cancel(
                            message = "Error fetching images",
                            cause = firebaseFirestoreException
                        )
                        return@addSnapshotListener
                    }
                    val images = querySnapshot?.documents?.map { it[Constants.FIELD_URL] as String }
                    offer(images)
                }
            awaitClose {
                listenerRegistration.remove()
            }
        }
    }
}