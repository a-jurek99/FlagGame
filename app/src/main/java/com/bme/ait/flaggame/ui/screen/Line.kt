package com.bme.ait.flaggame.ui.screen

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color


data class Line(
    val start: Offset,
    val end: Offset,
    val color: Color,
    val strokeWidth: Float = 5f
)
