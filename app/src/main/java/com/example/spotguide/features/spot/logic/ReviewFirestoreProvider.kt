package com.example.spotguide.features.spot.logic

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await

class ReviewFirestoreProvider(
    private val db: FirebaseFirestore,
    private val storageReference: StorageReference
) {

    private val collectionReference: CollectionReference
        get() = db.collection("reviews")


    suspend fun addReview(review: Review): Unit? {
        return try {
            collectionReference.document().set(review.toUploadModel()).await()
            Unit
        } catch (e: Exception) { null }
    }

    suspend fun getReviewsForSpot(spotId: String): List<Review>? {
        return try {
            val query = collectionReference.whereEqualTo("spotId", spotId)
            val result = query.get().await()
            val reviews = result.toObjects(Review::class.java)
            reviews
        } catch (e: Exception) { null }
    }

}