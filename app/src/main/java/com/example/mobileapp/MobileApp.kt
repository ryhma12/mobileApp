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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mobileapp.ui.screens.ChatScreen
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

    //TODO Use res
    val navItems = listOf(
        NavItem(
            label = "Home",
            route = "home_route",
            icon = Icons.Filled.Home,
            showInBottomBar = true
        ),
        NavItem(
            label = "Matches",
            route = "match_route",
            icon = Icons.Filled.Check,
            showInBottomBar = true
        ),
        NavItem(
            label = "Search",
            route = "search_route",
            icon = Icons.Filled.Search,
            showInBottomBar = true
        ),
        NavItem(
            label = "Contacts",
            route = "contacts_route",
            icon = Icons.Filled.MailOutline,
            showInBottomBar = true
        ),
        NavItem(label = "Login", route = "login_route",),
        NavItem(label = "Update Profile", route = "updateUser_route"),
        NavItem(label = "Chat", route = "chat_route")
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(
                currentRoute = currentRoute,
                navController = navController,
                navItems = navItems
            )
        },
        bottomBar = {
            if(currentRoute in navItems.filter { it.showInBottomBar }.map {it.route})
            NavigationBar {
                navItems
                    .filter { it.showInBottomBar }
                    .forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.route,
                            icon = { Icon(item.icon!!, item.label) },
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
        NavHost(
            navController = navController,
            startDestination = "login_route",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home_route") { HomeScreen(navController) }
            composable("search_route") { SearchScreen(navController) }
            composable("contacts_route") { ContactsScreen(navController) }
            composable(
                "chat_route/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                val contactId = backStackEntry.arguments?.getInt("id") ?: 0
                ChatScreen(contactId = contactId)
            }
            composable("match_route") { MatchScreen(navController) }
            composable("settings_route") { SettingsScreen(navController) }
            composable("login_route") { LoginScreen(navController) }
            composable("updateUser_route") { UpdateUserInfoScreen(navController) }
        }
    }
}

data class NavItem(
    val label: String,
    val icon: ImageVector? = null,
    val route: String,
    val showInBottomBar: Boolean = false
)

