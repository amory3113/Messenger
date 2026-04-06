package com.example.messenger.ul.navigation

sealed class Screen(val route: String){
    object Login : Screen("login_screen")
    object SignUp : Screen("signup_screen")
    object ChatList : Screen("chat_list_screen")
    object Search : Screen("search_screen")
    object Profile : Screen("profile_screen")
    object Chat : Screen("chat_screen")
}