package com.example.vociapp.data.remote

import com.example.vociapp.data.types.Homeless
import com.example.vociapp.data.types.Request
import com.example.vociapp.data.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.collections.get
import kotlin.text.get

class FirestoreDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun addRequest(request: Request): Resource<String> {
        return try {
            val documentReference = firestore.collection("requests").add(request).await()
            Resource.Success(documentReference.id)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    suspend fun getRequests(): Resource<List<Request>> {
        return try {
            val requests = firestore.collection("requests").get().await()
                .toObjects(Request::class.java)
            Resource.Success(requests)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    suspend fun updateRequest(request: Request): Resource<Unit> {
        return try {
            val querySnapshot = firestore.collection("requests")
                .whereEqualTo("id", request.id) // Query by "id" field
                .get()
                .await()

            if (querySnapshot.documents.isNotEmpty()) {
                val documentId = querySnapshot.documents[0].id // Get the document ID
                firestore.collection("requests")
                    .document(documentId) // Use the document ID for update
                    .set(request, SetOptions.merge())
                    .await()
                Resource.Success(Unit)
            } else {
                Resource.Error("Request not found") // Handle case where request is not found
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    suspend fun deleteRequest(request: Request): Resource<Unit> {
        return try {
            val querySnapshot = firestore.collection("requests")
                .whereEqualTo("id", request.id) // Query by "id" field
                .get()
                .await()

            if (querySnapshot.documents.isNotEmpty()) {
                val documentId = querySnapshot.documents[0].id // Get the document ID
                firestore.collection("requests")
                    .document(documentId) // Use the document ID for deletion
                    .delete()
                    .await()
                Resource.Success(Unit)
            } else {
                Resource.Error("Request not found") // Handle case where request is not found
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }


    suspend fun addHomeless(homeless: Homeless): Resource<String> {
        return try {
            val documentReference = firestore.collection("homelesses").add(homeless).await()
            Resource.Success(documentReference.id)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    suspend fun getHomelesses(): Resource<List<Homeless>> {
        return try {
            val homelesses = firestore.collection("homelesses").get().await()
                .toObjects(Homeless::class.java)
            Resource.Success(homelesses)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    suspend fun getHomeless(homelessID: String): Homeless? {
        return try {
            val querySnapshot = firestore.collection("homelesses")
                .whereEqualTo("id", homelessID)
                .get()
                .await()

            if (querySnapshot.documents.isNotEmpty()) {
                val documentId = querySnapshot.documents[0].id
                val homeless = firestore.collection("homelesses")
                    .document(documentId)
                    .get()
                    .await()
                    .toObject(Homeless::class.java)
                homeless // Return the Homeless object if found
            } else {
                null // Return null if homeless not found
            }
        } catch (e: Exception) {
            // Handle exception, e.g., log the error
            // and return null or throw an exception
            null
        }
    }

}