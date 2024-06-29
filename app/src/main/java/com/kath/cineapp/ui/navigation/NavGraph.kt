package com.kath.cineapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kath.cineapp.ui.features.candystore.CandyStoreScreen
import com.kath.cineapp.ui.features.home.HomeScreen
import com.kath.cineapp.ui.features.login.GoogleSignInClient
import com.kath.cineapp.ui.features.login.LoginScreen
import com.kath.cineapp.ui.features.login.LoginViewModel
import com.kath.cineapp.ui.features.payment.PaymentScreen
import com.kath.cineapp.ui.features.payment.PaymentState
import kotlinx.coroutines.CoroutineScope
import org.koin.androidx.compose.koinViewModel


@Composable
fun SetupNavGraph(
    navController: NavHostController,
    modifier: Modifier,
    coroutineScope: CoroutineScope,
) {
    val context = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(onTapMovie = {
                navController.navigate(Screen.Login.route)
            })
        }

        composable(route = Screen.Login.route) {
            val loginViewModel: LoginViewModel = koinViewModel()
            LoginScreen(
                onGoogleSignIn = {
                    GoogleSignInClient(context, coroutineScope).signIn {
                        loginViewModel.saveUser(it)
                    }
                },
                viewModel = loginViewModel,
                goToStore = {
                    navController.navigate(Screen.CandyStore.route)
                },
                goToMain = {
                    navController.navigate(Screen.Home.route)
                })
        }

        composable(route = Screen.CandyStore.route) {
            CandyStoreScreen(onTapContinue = {
                navController.navigate(Screen.Payment.route)
            })
        }

        composable(route = Screen.Payment.route) {
            PaymentScreen()
        }
    }
}

