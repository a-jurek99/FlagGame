package com.bme.ait.flaggame.ui.screen.menuScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    viewModel: MenuViewModel,
    onPlayClick: () -> Unit
) {
    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("Flag Guessing Game") },
            )
        }
    ) { innerPadding ->
        Column(
            modifier  = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            //logo of some kind?
            TextButton(
                onClick = onPlayClick
            ){
                Text("Play")
            }
            //if high score exists, display
        }
    }
}
