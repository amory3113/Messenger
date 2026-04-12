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

@Composable
fun MainNavGraph(navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
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
        composable(route = Screen.ChatList.route){
            PlaceHolderScreen(
                title = stringResource(id = R.string.chat_list),
                buttonText = stringResource(id = R.string.search_friends),
                onClick = { navController.navigate(Screen.Search.route)}
            )
        }
        composable(route = Screen.Search.route){
            PlaceHolderScreen(
                title = stringResource(id = R.string.search_users),
                buttonText = stringResource(id = R.string.back),
                onClick = { navController.navigate(Screen.ChatList.route)}
            )
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