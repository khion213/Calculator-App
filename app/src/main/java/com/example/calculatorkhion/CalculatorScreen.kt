package com.example.calculatorkhion

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.foundation.text.BasicTextField

@Composable
fun CalculatorScreen(viewModel: CalculatorViewModel = viewModel()) {
    val display by viewModel.display.observeAsState(TextFieldValue(""))
    val buttonColors by viewModel.buttonColors.observeAsState(List(20) { 0 })
    val isSystemInDarkTheme = isSystemInDarkTheme()

    val buttonBaseColors = if (isSystemInDarkTheme) {
        listOf(
            Color(0xFFB0BEC5),
            Color(0xFFCFD8DC),
            Color(0xFFB3E5FC),
            Color(0xFFB2EBF2),
            Color(0xFFB2DFDB)
        )
    } else {
        listOf(
            Color(0xFFB2DFDB),
            Color(0xFFFFF9C4),
            Color(0xFFFFCCBC),
            Color(0xFFC8E6C9),
            Color(0xFFD1C4E9)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isSystemInDarkTheme) Color.Black else Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Display
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(if (isSystemInDarkTheme) Color.DarkGray else Color.LightGray)
                .padding(16.dp)
                .heightIn(max =240.dp)
        ) {
            BasicTextField(
                value = display,
                onValueChange = { },
                textStyle = TextStyle(
                    fontSize = 36.sp,
                    color = if (isSystemInDarkTheme) Color.White else Color.Black,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 48.sp
                ),
                readOnly = true,
                enabled = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {

                    }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            val buttons = listOf(
                listOf("AC", "√", "÷", "Exit"),
                listOf("7", "8", "9", "x"),
                listOf("4", "5", "6", "-"),
                listOf("1", "2", "3", "+"),
                listOf("0", ".", "⌫", "=")
            )

            for ((rowIndex, row) in buttons.withIndex()) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    for ((colIndex, button) in row.withIndex()) {
                        val colorIndex = rowIndex * 4 + colIndex
                        val buttonColor = Color(buttonColors[colorIndex])

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.weight(1f)
                                .aspectRatio(1f)
                                .padding(8.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(buttonColor)
                                .clickable {
                                    viewModel.shuffleColors()
                                    when (button) {
                                        in "0".."9" -> viewModel.onDigit(button[0])
                                        "AC" -> viewModel.onClear()
                                        "=" -> viewModel.onEqual()
                                        "." -> viewModel.onDecimalPoint()
                                        "⌫" -> viewModel.onBackspace()
                                        "√" -> viewModel.onSquareRoot()
                                        "Exit" -> viewModel.onExit()
                                        else -> viewModel.onOperator(
                                            when (button) {
                                                "x" -> 'x'
                                                "÷" -> '÷'
                                                else -> button[0]
                                            }
                                        )
                                    }
                                }
                        ) {
                            Text(
                                text = button,
                                fontSize = 24.sp,
                                color = if (isSystemInDarkTheme) Color.White else Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}