package com.example.spotguide.core

import com.example.spotguide.features.spot.logic.SpotFirestoreProvider
import com.example.spotguide.features.spot.logic.SpotRepository
import com.example.spotguide.features.spot.logic.SpotRepositoryImp
import com.example.spotguide.features.spot.logic.SpotViewModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.FirebaseStorage
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModules = module {

    // ViewModels
    viewModel { SpotViewModel( get() ) }

    // Repositories
    single<SpotRepository> { SpotRepositoryImp( get() ) }

    // Providers
    factory { SpotFirestoreProvider( get(), get() ) }

    // Firebase
    single {
        val db = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
            .build()
        db.firestoreSettings = settings
        db
    }
    factory { FirebaseStorage.getInstance().reference }
    factory { FirebaseDatabase.getInstance().reference }
}