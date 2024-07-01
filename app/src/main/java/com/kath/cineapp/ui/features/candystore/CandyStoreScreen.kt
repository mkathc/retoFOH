package com.kath.cineapp.ui.features.candystore

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.koin.androidx.compose.koinViewModel

@Composable
fun CandyStoreScreen(
    onTapContinue: (Double) -> Unit,
    viewModel: CandyStoreViewModel = koinViewModel()
) {
    val state = viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.isLoggedUser()
    }
    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            if (state.value is StoreUiState.Success) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Resumen",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight(600)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Absolute.SpaceBetween
                    ) {
                        Text(
                            text = "Entrada:",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = " S/. 16.00",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Absolute.SpaceBetween
                    ) {
                        Text(
                            text = "Adicionales:",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "S/. ${viewModel.currentProductsState().totalProducts}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Absolute.SpaceBetween
                    ) {
                        Text(
                            text = "Monto total:",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "S/. ${viewModel.currentProductsState().totalAmount}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            onTapContinue(viewModel.currentProductsState().totalAmount)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                        shape = RoundedCornerShape(30.dp),
                        contentPadding = PaddingValues(16.dp),
                        enabled = true
                    ) {
                        Text(
                            text = "Continuar",
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }
            }
        }
    ) {
        when (state.value) {
            StoreUiState.Error -> {
                Column(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Error al obtener los datos",
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            StoreUiState.Loading -> {
                LaunchedEffect(Unit) {
                    viewModel.getCandyStore()
                }
                Column(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .width(64.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }

            is StoreUiState.Success -> {
                val result = state.value as StoreUiState.Success

                Column(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize()
                ) {
                    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                        items(result.storeList.size) { index ->
                            Column(modifier = Modifier.clickable {
                                viewModel.addProduct(result.storeList[index])
                            }) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(result.storeList[index].image)
                                        .build(),
                                    contentDescription = "Image",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                )

                                Text(
                                    text = result.storeList[index].description,
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp, vertical = 10.dp),
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = result.storeList[index].getFormattedPrice(),
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp),
                                    fontSize = 20.sp,
                                    color = MaterialTheme.colorScheme.secondary,
                                    fontWeight = FontWeight(600)
                                )
                            }

                        }
                    }
                }
            }

            StoreUiState.IsNotLogged -> {
                Column(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Usted no está logueado",
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}