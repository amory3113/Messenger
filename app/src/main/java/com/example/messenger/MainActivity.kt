package com.example.messenger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.navigation.compose.rememberNavController
import com.example.messenger.ul.feature.chat.PresenceManager
import com.example.messenger.ul.navigation.MainNavGraph
import com.example.messenger.ul.theme.MessengerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                PresenceManager.updateStatus(true)
            }

            override fun onStop(owner: LifecycleOwner) {
                PresenceManager.updateStatus(false)
            }
        })
        enableEdgeToEdge()
        setContent {
            MessengerTheme {
               val navController = rememberNavController()
                MainNavGraph(navController = navController)
            }
        }
    }
}