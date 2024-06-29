package com.kath.cineapp.ui.features.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(onTapMovie: () -> Unit, viewModel: HomeViewModel = koinViewModel()) {
    val state = viewModel.state.collectAsState()
    Scaffold(
        containerColor = Color.White,
        topBar = {
            Text(text = "HomeScreen")
        }
    ) {
        when (state.value) {
            HomeUiState.Error -> {
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

            HomeUiState.Loading -> {
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

            is HomeUiState.Success -> {
                Column(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize()
                ) {
                    val pagerState = rememberPagerState(pageCount = {
                        (state.value as HomeUiState.Success).premieresList.size
                    })

                    HorizontalPager(
                        modifier = Modifier
                            .fillMaxSize(), state = pagerState
                    ) { index ->
                        Box(modifier = Modifier.clickable {
                            onTapMovie()
                        }) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data((state.value as HomeUiState.Success).premieresList[index].image)
                                    .build(),
                                contentDescription = "Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                                    .clip(RoundedCornerShape(16.dp))
                            )

                            Text(
                                text = (state.value as HomeUiState.Success).premieresList[index].description,
                                modifier = Modifier
                                    .padding(25.dp)
                                    .align(Alignment.BottomStart),
                                color = Color.White,
                                fontSize = 20.sp
                            )
                        }

                    }
                }
            }
        }
    }
}