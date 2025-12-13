package com.bme.ait.flaggame

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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bme.ait.flaggame.ui.screen.gameScreen.GameScreen
import com.bme.ait.flaggame.ui.screen.gameScreen.GameViewModel
import com.bme.ait.flaggame.ui.screen.menuScreen.MenuScreen
import com.bme.ait.flaggame.ui.screen.menuScreen.MenuViewModel
import com.bme.ait.flaggame.ui.theme.FlagGameTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlagGameTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "menu") {
                    composable("menu") {
                        MenuScreen(
                            onPlayClick = {
                                navController.navigate("game")
                            }
                        )
                    }
                    composable("game") {
                        GameScreen(
                            viewModel = viewModel(),
                            onNavigateBack = {
                                navController.navigate("menu")
                            }
                        )
                    }
                }
            }
        }
    }
}
