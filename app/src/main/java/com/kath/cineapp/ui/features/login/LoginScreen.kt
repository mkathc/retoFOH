package com.kath.cineapp.ui.features.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.kath.cineapp.ui.features.login.model.LoginViewModel

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
                            modifier = Modifier
                                .fillMaxWidth().padding(20.dp)
                                .wrapContentHeight().align(Alignment.CenterHorizontally),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                            ),
                            shape = RoundedCornerShape(30.dp),
                            contentPadding = PaddingValues(16.dp),
                            enabled = true
                        ) {
                            Row(
                                modifier = Modifier.padding(5.dp),
                            ) {
                                Icon(
                                    contentDescription = "google icon",
                                    painter = painterResource(id = R.drawable.google),
                                    modifier = Modifier.height(25.dp)
                                )
                                Text(
                                    text = "Log in con Google",
                                    modifier = Modifier.padding(horizontal = 10.dp)
                                )
                            }
                        }

                        Text(text = "o", modifier = Modifier.align(Alignment.CenterHorizontally))

                        Button(
                            onClick = onGoogleSignIn,
                            modifier = Modifier
                                .fillMaxWidth().padding(20.dp)
                                .wrapContentHeight().align(Alignment.CenterHorizontally),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                            ),
                            shape = RoundedCornerShape(30.dp),
                            contentPadding = PaddingValues(16.dp),
                            enabled = true
                        ) {
                            Row(
                                modifier = Modifier.padding(5.dp),
                            ) {
                                Text(
                                    text = "Invitado",
                                    modifier = Modifier.padding(horizontal = 10.dp)
                                )
                            }
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
                            .padding(horizontal = 16.dp)
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