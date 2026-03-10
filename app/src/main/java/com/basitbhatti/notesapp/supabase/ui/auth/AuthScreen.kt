package com.basitbhatti.notesapp.supabase.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.basitbhatti.notesapp.supabase.ui.theme.NotesAppSupabaseTheme

@Composable
fun AuthScreen(onAuthSuccess: () -> Unit) {

    val viewModel: AuthViewModel = viewModel()

    val uiState by viewModel.uiState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(uiState) {
        if (uiState is AuthState.Success) onAuthSuccess()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        OutlinedTextField(
            modifier = Modifier.padding(horizontal = 15.dp).fillMaxWidth(),
            value = email,
            onValueChange = {
                email = it
            },
            placeholder = {
                Text("Email")
            }
        )

        Spacer(Modifier.height(15.dp))

        OutlinedTextField(
            modifier = Modifier.padding(horizontal = 15.dp).fillMaxWidth(),
            value = password,
            onValueChange = {
                password = it
            },
            placeholder = {
                Text("Password")
            }
        )

        Spacer(Modifier.height(15.dp))


        Button(
            modifier = Modifier.padding(horizontal = 15.dp).fillMaxWidth(),
            onClick = {
                viewModel.signIn(email, password)
            }
        ) {
            Text("Sign In")
        }



        Spacer(Modifier.height(15.dp))

        Text("Or")


        Spacer(Modifier.height(15.dp))


        Button(
            modifier = Modifier.padding(horizontal = 15.dp).fillMaxWidth(),
            onClick = {
                viewModel.signUp(email, password)
            }
        ) {
            Text("Sign Up")
        }


        Spacer(Modifier.height(15.dp))


        if (uiState is AuthState.Error){

            Spacer(Modifier.height(12.dp))

            Text(text = (uiState as AuthState.Error).message, color = MaterialTheme.colorScheme.error)

        }


    }

}


@Preview
@Composable
private fun AuthPrev() {

    NotesAppSupabaseTheme {
        AuthScreen {

        }
    }

}