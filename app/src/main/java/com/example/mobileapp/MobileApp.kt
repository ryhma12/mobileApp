package com.example.mobileapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.internal.composableLambda
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mobileapp.ui.screens.ContactsScreen
import com.example.mobileapp.ui.screens.HomeScreen
import com.example.mobileapp.ui.screens.LoginScreen
import com.example.mobileapp.ui.screens.MatchScreen
import com.example.mobileapp.ui.screens.SearchScreen
import com.example.mobileapp.ui.screens.SettingsScreen
import com.example.mobileapp.ui.screens.UpdateUserInfoScreen
import com.example.mobileapp.ui.shared.TopBar

@Composable
fun MobileApp(navController: NavHostController = rememberNavController()) {
    val backstackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backstackEntry?.destination?.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            //TODO Unique topAppBar for each screen
            TopBar(navController = navController)
        },
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEach { (label, icon, route) ->
                    NavigationBarItem(
                        selected = currentRoute == route,
                        icon = { Icon(icon, contentDescription = label) },
                        onClick = {
                            navController.navigate(route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "updateUser_route",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home_route") { HomeScreen(navController) }
            composable("search_route") { SearchScreen(navController) }
            composable("contacts_route") { ContactsScreen(navController) }
            composable("match_route") { MatchScreen(navController) }
            composable("settings_route") { SettingsScreen(navController) }
            composable("login_route") { LoginScreen(navController) }
            composable("updateUser_route") { UpdateUserInfoScreen(navController) }
        }
    }
}

data class NavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

//TODO Use res
val bottomNavItems = listOf(
    NavItem(
        label = "Home",
        icon = Icons.Filled.Home,
        route = "home_route"
    ),
    NavItem(
        label = "Matches",
        icon = Icons.Filled.Check,
        route = "match_route"
    ),
    NavItem(
        label = "Search",
        icon = Icons.Filled.Search,
        route = "search_route"
    ),
    NavItem(
        label = "Contacts",
        icon = Icons.Filled.MailOutline,
        route = "contacts_route"
    ),
)