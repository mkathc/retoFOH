package com.kath.cineapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.kath.cineapp.ui.features.candystore.CandyStoreScreen
import com.kath.cineapp.ui.features.final_transaction.TransactionSuccessScreen
import com.kath.cineapp.ui.features.home.HomeScreen
import com.kath.cineapp.ui.features.login.GoogleSignInClient
import com.kath.cineapp.ui.features.login.LoginScreen
import com.kath.cineapp.ui.features.login.model.LoginViewModel
import com.kath.cineapp.ui.features.payment.PaymentScreen
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
                navController.navigate(Screen.Login.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }, viewModel = koinViewModel())
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
                    navController.navigate(Screen.CandyStore.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                goToMain = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
        }

        composable(route = Screen.CandyStore.route) {
            CandyStoreScreen(onTapContinue = {
                navController.navigate(
                    Screen.Payment.route.replace(
                        oldValue = "{total}",
                        newValue = it.toString()
                    )
                )
            })
        }

        composable(
            route = Screen.Payment.route,
            arguments = listOf(
                navArgument("total") {
                    defaultValue = 0.0
                    type = NavType.FloatType
                }
            )
        ) { backStackEntry ->
            PaymentScreen(
                total = backStackEntry.arguments?.getFloat("total")?.toDouble() ?: 0.0,
                onPaymentSuccess = {
                    navController.navigate(Screen.TransactionSuccess.route){
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                closePaymentScreen = {
                    navController.popBackStack()
                })
        }

        composable(route = Screen.TransactionSuccess.route) {
            TransactionSuccessScreen(onTapContinue = {
                navController.navigate(Screen.Home.route){
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            })
        }
    }
}

