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


    fun notesFlow(scope: CoroutineScope): Flow<List<Note>> = flow {

        emit(db["notes"].select().decodeList<Note>())

        val channel = realtime.channel("notes-changes")

        channel.postgresChangeFlow<PostgresAction.Insert>(schema = "public"){
            table = "notes"
        }.onEach {
            emit(db["notes"].select().decodeList<Note>())
        }.launchIn(scope)

        channel.postgresChangeFlow<PostgresAction.Delete>(schema = "public"){
            table = "notes"
        }.onEach {
            emit(db["notes"].select().decodeList<Note>())
        }.launchIn(scope)

        channel.postgresChangeFlow<PostgresAction.Update>(schema = "public"){
            table = "notes"
        }.onEach {
            emit(db["notes"].select().decodeList<Note>())
        }.launchIn(scope)

        channel.subscribe()

        awaitCancellation()

    }.flowOn(Dispatchers.IO)

    suspend fun getNotes(): List<Note> =
        db["notes"].select().decodeList<Note>()

    suspend fun addNote(title: String, content: String) {
        val userId = SupabaseClient.client.auth.currentUserOrNull()?.id ?: return
        db["notes"].insert(
            Note(title = title, content = content, userId = userId)
        )
    }

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