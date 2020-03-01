package com.example.spotguide.features.spot.logic

import com.example.spotguide.features.spot.Spot
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await

class SpotFirestoreProvider(
    private val db: FirebaseFirestore,
    private val storageReference: StorageReference
) {

    private val collectionReference: CollectionReference
        get() = db.collection("spots")

    suspend fun getAllSpots(): List<Spot>? {
        return try {
            val result = collectionReference.get().await()
            val spots = result.toObjects(Spot::class.java)
            result.documents.forEachIndexed { index, documentSnapshot ->
                spots[index].id = documentSnapshot.id
            }
            spots
        } catch (e: Exception) { null }
    }

    suspend fun addSpot(spot: Spot): Unit? {
        // TODO add photos to firebase
        return try {
            collectionReference.document().set(spot.toUploadModel()).await()
            Unit
        } catch (e: Exception) { null }
    }

}