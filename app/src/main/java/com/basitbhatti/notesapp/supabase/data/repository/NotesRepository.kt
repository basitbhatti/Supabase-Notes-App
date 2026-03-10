package com.basitbhatti.notesapp.supabase.data.repository

import com.basitbhatti.notesapp.supabase.data.SupabaseClient
import com.basitbhatti.notesapp.supabase.data.model.Note
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest

class NotesRepository {

    private val db = SupabaseClient.client.postgrest

    suspend fun getNotes(): List<Note> =
        db["notes"].select().decodeList<Note>()

    suspend fun addNote(title: String, content: String){
        val userId = SupabaseClient.client.auth.currentUserOrNull()?.id ?: return
        db["notes"].insert(
            Note(title = title, content = content, userId = userId)
        )
    }

    suspend fun deleteNote(id: String){
        db["notes"].delete { filter { eq("id", id) } }
    }

    suspend fun updateNote(id: String, title: String, content: String){
        db["notes"].update({
            set("title", title)
            set("content", content)
        }) {
            filter { eq("id", id) }
        }
    }

}