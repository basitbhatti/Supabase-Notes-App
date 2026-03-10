package com.basitbhatti.notesapp.supabase.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Note(
    val id: String = "",
    @SerialName("user_id") val userId: String = "",
    val title: String,
    val content: String,
    @SerialName("created_at") val createdAt: String = ""
)