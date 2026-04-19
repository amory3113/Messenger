package com.example.messenger.ul.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.messenger.R
import com.example.messenger.ul.feature.MainScreen
import com.example.messenger.ul.feature.auth.LoginScreen
import com.example.messenger.ul.feature.auth.SignUpScreen
import com.example.messenger.ul.feature.chat.ChatScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MainNavGraph(navController: NavHostController){
    val currentUser = FirebaseAuth.getInstance().currentUser
    val startDestination = if (currentUser != null) Screen.Main.route else Screen.Login.route

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(route = Screen.SignUp.route) {
            SignUpScreen(navController = navController)
        }
        composable(route = Screen.Main.route) {
            MainScreen(rootNavController = navController)
        }
        composable(route = Screen.Chat.route + "/{chatId}") { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
            ChatScreen(navController = navController, chatId = chatId)
        }
    }
}

@Composable
fun PlaceHolderScreen(title: String, buttonText: String, onClick: () -> Unit){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(text = title)
            Button(onClick = onClick) {
                Text(text = buttonText)
            }
        }
    }
}