package com.basitbhatti.notesapp.supabase.data.repository

import com.basitbhatti.notesapp.supabase.data.SupabaseClient
import io.github.jan.supabase.storage.storage
import kotlin.time.Duration.Companion.seconds

class StorageRepository {

    private val storage = SupabaseClient.client.storage
    private val bucket = "notes-images"

    suspend fun uploadImage(
        userId: String,
        noteId: String,
        imageBytes: ByteArray
    ): Result<String?> = runCatching {
        val path = "$userId/$noteId.jpg"

        storage[bucket].upload(
            path = path,
            data = imageBytes
        )

        path
    }

    suspend fun getSignedUrl(path: String): Result<String> = runCatching {
        storage[bucket].createSignedUrl(
            path = path,
            expiresIn = 60.seconds
        )
    }

    suspend fun deleteImage(path: String): Result<Unit> = runCatching {
        storage[bucket].delete(listOf(path))
    }


}