package com.example.spotguide.core

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.FirebaseStorage
import org.koin.dsl.module

val appModules = module {

    // ViewModels
//    viewModel { EventViewModel(get()) }

    // Repositories
//    single<EventRepository> { EventRepositoryImp(get() ) }

    // Providers
//    factory { UserProvider(get(), get()) }

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