package com.example.spotguide.core.extension

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

@ExperimentalCoroutinesApi
suspend fun DatabaseReference.getValue(): DataSnapshot {
    return suspendCancellableCoroutine { coroutine ->
        addListenerForSingleValueEvent(FValueEventListener(
            onDataChange = { coroutine.resume(it, {}) },
            onError = { coroutine.resumeWithException(it.toException()) }
        ))
    }
}

class FValueEventListener(val onDataChange: (DataSnapshot) -> Unit, val onError: (DatabaseError) -> Unit) :
    ValueEventListener {
    override fun onDataChange(data: DataSnapshot) = onDataChange.invoke(data)
    override fun onCancelled(error: DatabaseError) = onError.invoke(error)
}