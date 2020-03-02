package com.example.spotguide.core

import com.example.spotguide.features.galley.logic.ImagesRepository
import com.example.spotguide.features.galley.logic.ImagesRepositoryImp
import com.example.spotguide.features.galley.logic.ImagesViewModel
import com.example.spotguide.features.spot.logic.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.FirebaseStorage
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModules = module {

    // ViewModels
    viewModel { SpotViewModel( get() ) }
    viewModel { ImagesViewModel( get() ) }

    // Repositories
    single<SpotRepository> { SpotRepositoryImp( get(), get() ) }
    single<ImagesRepository> { ImagesRepositoryImp() }

    // Providers
    factory { SpotFirestoreProvider( get(), get() ) }
    factory { ReviewFirestoreProvider( get(), get() ) }

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