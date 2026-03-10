package com.basitbhatti.notesapp.supabase.data.repository

import com.basitbhatti.notesapp.supabase.data.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email

class AuthRepository() {
    private val auth = SupabaseClient.client.auth

    suspend fun signUp(email: String, password: String): Result<Unit> = runCatching {
        auth.signUpWith(Email) {
            this.email = email
            this.password = password
        }
    }

    suspend fun signIn(email: String, password: String): Result<Unit> = runCatching {
        auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
    }

    suspend fun signOut() = auth.signOut()

    fun createUser() = auth.currentUserOrNull()
}