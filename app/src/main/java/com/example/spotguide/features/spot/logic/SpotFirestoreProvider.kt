package com.example.spotguide.features.spot.logic

import com.example.spotguide.features.spot.Spot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference

class SpotFirestoreProvider(
    private val db: FirebaseFirestore,
    private val storageReference: StorageReference
) {

    suspend fun getAllSpots(): List<Spot>? {

        return emptyList()
    }

}