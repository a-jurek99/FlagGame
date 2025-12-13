package com.bme.ait.flaggame.ui.screen.gameScreen

import android.graphics.Bitmap
import android.graphics.Picture
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bme.ait.flaggame.ui.screen.Line
import com.bme.ait.flaggame.ui.theme.flagBlue
import com.bme.ait.flaggame.ui.theme.flagGreen
import com.bme.ait.flaggame.ui.theme.flagRed
import com.bme.ait.flaggame.ui.theme.flagYellow

private val gameCountries = listOf("The United States", "Hungary", "Germany", "Japan", "Brazil", "Nigeria", "Bhutan", "Poland", "Italy", "Singapore").shuffled()
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onNavigateBack: () -> Unit,
) {
    val lines = remember{ mutableStateListOf<Line>()}
    var currentColor by remember{mutableStateOf(Color.Red)}
    var strokeWidth by remember{mutableStateOf(10f)}

    var isSubmitted by remember {mutableStateOf(false)}
    var currentRound by remember {mutableStateOf(0)}
    var sumScore by remember {mutableStateOf(0)}
    var currentFlag by remember {mutableStateOf("")}
    var endGameDialog by remember {mutableStateOf(false)}

    val score by viewModel.scoreResult.collectAsState()
    val isEvaluating by viewModel.isEvaluating.collectAsState()
    val picture = remember { Picture() }

    fun nextRound(){
        if (currentRound < 2) {
            lines.clear()
            isSubmitted = false
            currentFlag = gameCountries[currentRound]
            currentRound++
            viewModel.resetScore()
        } else {
            endGameDialog = true
        }
    }
    LaunchedEffect(score){
        if (score != null && score!! >= 0) {
            sumScore += score!!
        }
    }

    LaunchedEffect(Unit) {
        nextRound()
    }

    Scaffold(
        topBar = {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primary),
            title = {
                Text(
                    text = "Flag Guessing Game",
                    color = Color.White,
                    fontSize = 18.sp,
                )
            },
        )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Draw the flag of $currentFlag", fontSize = 24.sp)

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .background(currentColor)
                    .border(2.dp, Color.Black)
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val colors = listOf(flagRed, flagBlue, Color.White, flagGreen, flagYellow, Color.Black)
                    colors.forEach { color ->
                        Button(
                            colors = ButtonDefaults.buttonColors(color),
                            onClick = { if (!isSubmitted) currentColor = color },
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .size(40.dp)
                                .border(1.dp, Color.LightGray)
                        ) {}
                    }
                }
            }

            Spacer(modifier = Modifier.height(45.dp))

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f)
                    .border(width = 1.dp, color = Color.LightGray)
                    .clipToBounds()
                    .pointerInput(!isSubmitted) {
                        if (isSubmitted) return@pointerInput

                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            val line = Line(
                                start = change.position - dragAmount,
                                end = change.position,
                                color = currentColor,
                                strokeWidth = strokeWidth
                            )
                            lines.add(line)
                        }
                    }
            ) {
                drawIntoCanvas { canvas ->
                    val recordingCanvas = picture.beginRecording(size.width.toInt(), size.height.toInt())
                    lines.forEach { line ->
                        recordingCanvas.drawLine(
                            line.start.x,
                            line.start.y,
                            line.end.x,
                            line.end.y,
                            android.graphics.Paint().apply {
                                color = line.color.toArgb()
                                this.strokeWidth = line.strokeWidth
                                isAntiAlias = true
                                style = android.graphics.Paint.Style.STROKE
                                strokeCap = android.graphics.Paint.Cap.ROUND
                            }
                        )
                    }
                    picture.endRecording()
                    canvas.nativeCanvas.drawPicture(picture)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Slider(
                    value = strokeWidth,
                    onValueChange = { newWidth -> strokeWidth = newWidth },
                    valueRange = 5f..150f,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if(isSubmitted){
                when {
                    isEvaluating -> Text("Evaluating...", fontSize = 24.sp)
                    score != null -> {
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { nextRound() }) {
                            Text(if (currentRound < 5) "Next" else "Finish", fontSize = 24.sp)
                        }
                        Text("Round Score: $score", fontSize = 24.sp)
                    }
                }
            } else {
                Button(
                    onClick = {
                        isSubmitted = true
                        val bitmap = Bitmap.createBitmap(
                            picture.width,
                            picture.height,
                            Bitmap.Config.ARGB_8888
                        )
                        val canvas = android.graphics.Canvas(bitmap)
                        canvas.drawPicture(picture)
                        viewModel.evaluateDrawing(bitmap, currentFlag)
                    }
                ){
                    Text("Submit", fontSize = 24.sp)
                }
            }
        }
    }
    if(endGameDialog) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Game Over")},
            text = { Text("Score: $sumScore")},
            confirmButton = {
                TextButton(
                    onClick = {
                        endGameDialog = false
                        onNavigateBack()
                    }
                ){
                    Text("Exit")
                }
            }
        )
    }
}
