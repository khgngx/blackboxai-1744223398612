package com.example.temperatureapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TemperatureAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TemperatureScreen()
                }
            }
        }
    }
}

@Composable
fun TemperatureScreen(viewModel: TemperatureViewModel = viewModel()) {
    var temperatureInput by remember { mutableStateOf("") }
    val temperature = viewModel.temperature.value
    val weatherType = viewModel.weatherType.value
    val circleColor = viewModel.circleColor.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = temperatureInput,
            onValueChange = {
                temperatureInput = it
                viewModel.updateTemperature(it)
            },
            label = { Text("Enter Temperature (°C)") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(200.dp)
                .background(circleColor, shape = CircleShape)
        ) {
            Text(
                text = weatherType,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )
        }
    }
}

class TemperatureViewModel : ViewModel() {
    val temperature = mutableStateOf(0f)
    val weatherType = mutableStateOf("")
    val circleColor = mutableStateOf(Color.Blue)

    fun updateTemperature(input: String) {
        try {
            val temp = input.toFloat()
            temperature.value = temp
            when {
                temp < 25 -> {
                    weatherType.value = "Cold"
                    circleColor.value = Color.Blue
                }
                temp in 25f..28f -> {
                    weatherType.value = "Mild"
                    circleColor.value = Color.Green
                }
                temp in 29f..35f -> {
                    weatherType.value = "Hot"
                    circleColor.value = Color(0xFFFFA500) // Orange
                }
                temp > 35 -> {
                    weatherType.value = "Very Hot"
                    circleColor.value = Color.Red
                }
            }
        } catch (e: NumberFormatException) {
            // Handle invalid input
            weatherType.value = "Invalid"
            circleColor.value = Color.Gray
        }
    }
}

@Composable
fun TemperatureAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ColorScheme.light(),
        content = content
    )
}
