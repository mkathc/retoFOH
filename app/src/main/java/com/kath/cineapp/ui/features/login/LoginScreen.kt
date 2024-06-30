package com.kath.cineapp.ui.features.login

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kath.cineapp.R

@Composable
fun LoginScreen(
    onGoogleSignIn: () -> Unit,
    viewModel: LoginViewModel,
    goToStore: () -> Unit,
    goToMain: () -> Unit
) {
    val state = viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.isLoggedUser()
    }
    when (state.value) {
        LoginUiState.None -> {
            Scaffold(
                containerColor = Color.Black,
                bottomBar = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp)
                    ) {
                        Button(
                            onClick = onGoogleSignIn,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text(text = "Log in con Google")
                        }
                        Text(text = "o", modifier = Modifier.align(Alignment.CenterHorizontally))

                        Button(
                            onClick = { /*TODO*/ },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text(text = "Invitado")
                        }
                    }
                }
            ) {
                Column(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .clip(
                                RoundedCornerShape(16.dp)
                            )
                    )
                }
            }

        }

        LoginUiState.Success -> {
            viewModel.getUser()

            val openAlertDialog = remember { mutableStateOf(true) }

            when {
                openAlertDialog.value -> {
                    AlertDialog(
                        icon = {
                            Icon(Icons.Filled.VerifiedUser, contentDescription = "Example Icon")
                        },
                        title = {
                            Text(text = "Hola! " + viewModel.currentUser().name)
                        },
                        text = {
                            Text(text = "¿Deseas continuar con la compra?")
                        },
                        onDismissRequest = {
                            openAlertDialog.value = false
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    openAlertDialog.value = false
                                    goToStore()
                                }
                            ) {
                                Text("Aceptar")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    openAlertDialog.value = false
                                    goToMain()
                                }
                            ) {
                                Text("Dismiss")
                            }
                        }
                    )
                }
            }
        }

        is LoginUiState.Error -> {
            Text(text = (state.value as LoginUiState.Error).message)
        }
    }
}