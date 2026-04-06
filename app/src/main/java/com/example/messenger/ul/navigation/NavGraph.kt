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

@Composable
fun MainNavGraph(navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(route = Screen.Login.route){
            PlaceHolderScreen(
                title = stringResource(id = R.string.sign_up_screen),
                buttonText = stringResource(id = R.string.return_to_login),
                onClick = { navController.navigate(Screen.SignUp.route)}
            )
        }
        composable(route = Screen.SignUp.route){
            PlaceHolderScreen(
                title = stringResource(id = R.string.login_screen),
                buttonText = stringResource(id = R.string.go_to_registration),
                onClick = { navController.navigate(Screen.Login.route)}
            )
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