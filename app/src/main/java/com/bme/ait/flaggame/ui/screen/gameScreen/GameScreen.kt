package com.bme.ait.flaggame.ui.screen.gameScreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bme.ait.flaggame.ui.screen.Line

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    viewModel: GameViewModel
) {
    val lines = remember{ mutableStateListOf<Line>()}
    var currentColor by remember{mutableStateOf(Color.Red)}
    Column{
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primary),
            title = {
                Text(text = "Flag Guessing Game",
                    color = Color.White,
                    fontSize = 18.sp
                )
            },
            actions = {
                Button(
                    colors = ButtonDefaults.buttonColors(Color.Red),
                    onClick = {currentColor = Color.Red},
                    modifier = Modifier.padding(3.dp).width(40.dp)
                ) {
                    //
                }
                Button(
                    colors = ButtonDefaults.buttonColors(Color.Blue),
                    onClick = {currentColor = Color.Blue},
                    modifier = Modifier.padding(3.dp).width(40.dp)
                ) {
                    //
                }
                Button(
                    colors = ButtonDefaults.buttonColors(Color.White),
                    onClick = {currentColor = Color.White},
                    modifier = Modifier.padding(3.dp).width(40.dp)
                ) {
                    //
                }
                Button(
                    colors = ButtonDefaults.buttonColors(Color.Green),
                    onClick = {currentColor = Color.Green},
                    modifier = Modifier.padding(3.dp).width(40.dp)
                ) {
                    //
                }
                Button(
                    colors = ButtonDefaults.buttonColors(Color.Yellow),
                    onClick = {currentColor = Color.Yellow},
                    modifier = Modifier.padding(3.dp).width(40.dp)
                ) {
                    //
                }
                Button(
                    colors = ButtonDefaults.buttonColors(Color.Black),
                    onClick = {currentColor = Color.Black},
                    modifier = Modifier.padding(3.dp).width(40.dp)
                ) {
                    //
                }
            }
        )
        Canvas(
            modifier = Modifier.fillMaxSize()
                .pointerInput(true){
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        val line = Line(
                            start = change.position - dragAmount,
                            end = change.position,
                            color = currentColor,
                            strokeWidth = 5f
                        )
                        lines.add(line)
                    }
                }
        ){
            lines.forEach { line ->
                drawLine(
                    color = line.color,
                    start = line.start,
                    end = line.end,
                    strokeWidth = line.strokeWidth,
                    cap = StrokeCap.Round
                )
            }

        }
    }

}
