package com.kath.cineapp.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Login
import androidx.compose.material.icons.outlined.Store
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kath.cineapp.ui.navigation.Screen
import com.kath.cineapp.ui.navigation.SetupNavGraph
import com.kath.cineapp.ui.theme.CineAppTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val homeTab = TabBarItem(
                title = "Home",
                route = Screen.Home.route,
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home
            )
            val loginTab = TabBarItem(
                title = "Login",
                route = Screen.Login.route,
                selectedIcon = Icons.Filled.Login,
                unselectedIcon = Icons.Outlined.Login
            )
            val candyStoreTab = TabBarItem(
                title = "Candy Store",
                route = Screen.CandyStore.route,
                selectedIcon = Icons.Filled.Store,
                unselectedIcon = Icons.Outlined.Store
            )

            val tabBarItems = listOf(homeTab, loginTab, candyStoreTab)

            CineAppTheme {
                val navController = rememberNavController()

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            val backStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = backStackEntry?.destination

                            tabBarItems.forEach { item ->
                                NavigationBarItem(
                                    icon = { Icon(item.selectedIcon, contentDescription = null) },
                                    label = { Text(item.title) },
                                    selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                                    onClick = {
                                        navController.navigate(item.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    SetupNavGraph(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding),
                        coroutineScope = rememberCoroutineScope()
                    )
                }
            }
        }
    }
}

data class TabBarItem(
    val title: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)
