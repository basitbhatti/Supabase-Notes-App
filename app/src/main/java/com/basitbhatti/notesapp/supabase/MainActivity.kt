package com.basitbhatti.notesapp.supabase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.basitbhatti.notesapp.supabase.data.SupabaseClient
import com.basitbhatti.notesapp.supabase.ui.auth.AuthScreen
import com.basitbhatti.notesapp.supabase.ui.notes.NotesScreen
import com.basitbhatti.notesapp.supabase.ui.theme.NotesAppSupabaseTheme
import io.github.jan.supabase.auth.auth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotesAppSupabaseTheme {
                var isLoggedIn by remember {
                    mutableStateOf(
                        SupabaseClient.client.auth.currentUserOrNull() != null
                    )
                }

                if (isLoggedIn){
                    NotesScreen()
                } else {
                    AuthScreen {
                        isLoggedIn = true
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NotesAppSupabaseTheme {
        Greeting("Android")
    }
}