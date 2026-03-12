package com.basitbhatti.notesapp.supabase.data.repository

import com.basitbhatti.notesapp.supabase.data.SupabaseClient
import com.basitbhatti.notesapp.supabase.data.model.Note
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class NotesRepository {

    private val db = SupabaseClient.client.postgrest
    private val realtime = SupabaseClient.client.realtime

    private val storageRepo = StorageRepository()


    fun notesFlow(scope: CoroutineScope): Flow<List<Note>> = flow {

        emit(db["notes"].select().decodeList<Note>())

        val channel = realtime.channel("notes-changes")

        channel.postgresChangeFlow<PostgresAction.Insert>(schema = "public") {
            table = "notes"
        }.onEach {
            emit(db["notes"].select().decodeList<Note>())
        }.launchIn(scope)

        channel.postgresChangeFlow<PostgresAction.Delete>(schema = "public") {
            table = "notes"
        }.onEach {
            emit(db["notes"].select().decodeList<Note>())
        }.launchIn(scope)

        channel.postgresChangeFlow<PostgresAction.Update>(schema = "public") {
            table = "notes"
        }.onEach {
            emit(db["notes"].select().decodeList<Note>())
        }.launchIn(scope)

        channel.subscribe()

        awaitCancellation()

    }.flowOn(Dispatchers.IO)

    suspend fun getNotes(): List<Note> =
        db["notes"].select().decodeList<Note>()

    suspend fun addNote(title: String, content: String, imageBytes: ByteArray? = null) {
        val userId = SupabaseClient.client.auth.currentUserOrNull()?.id ?: return

        val note = db["notes"].insert(
            Note(title = title, content = content, userId = userId)
        ) { select() }.decodeSingle<Note>()

        if (imageBytes != null){
            storageRepo.uploadImage(
                userId, note.id, imageBytes
            ).onSuccess { path ->
                db["notes"].update({
                    set("image_path", path)
                }) {
                    filter { eq("id", note.id) }
                }
            }
        }

    }

    suspend fun deleteNote(id: String, imagePath: String?){
        db["notes"].delete {
            filter { eq("id", id) }
            imagePath?.let { storageRepo.deleteImage(it) }
        }
    }

    suspend fun getSignedImageUrl(path: String): String? =
        storageRepo.getSignedUrl(path).getOrNull()

    suspend fun deleteNote(id: String) {
        db["notes"].delete { filter { eq("id", id) } }
    }

    suspend fun updateNote(id: String, title: String, content: String) {
        db["notes"].update({
            set("title", title)
            set("content", content)
        }) {
            filter { eq("id", id) }
        }
    }

}