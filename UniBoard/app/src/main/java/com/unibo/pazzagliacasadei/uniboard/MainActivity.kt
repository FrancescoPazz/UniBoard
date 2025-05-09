package com.unibo.pazzagliacasadei.uniboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.unibo.pazzagliacasadei.uniboard.ui.navigation.UniBoardNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            UniBoardNavGraph(
                navController = navController
            )
        }
    }
}