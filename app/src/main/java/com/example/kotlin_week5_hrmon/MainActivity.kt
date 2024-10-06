package com.example.kotlin_week5_hrmon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.kotlin_week5_hrmon.ui.theme.Kotlinweek5hrmonTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Kotlinweek5hrmonTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CalorieScreen()
                }
            }
        }
    }
}

@Composable
fun Heading(title: String) {
    Text(
        text = title,
        fontSize = 24.sp,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp)
    )
}

@Composable
fun CalorieScreen() {
    val weightInput = remember { mutableStateOf("") }
    var weight = weightInput.value.toIntOrNull() ?: 0
    val male = remember { mutableStateOf(true) }
    val intensity = remember { mutableStateOf(1.3f) }
    val result = remember { mutableStateOf(0) }
    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Heading(title = stringResource(id = R.string.app_name))
        WeightField(weightInput = weightInput.value, onValueChange = { weightInput.value = it })
        GenderChoices(male.value, setGenderMale = { male.value = it })
        IntensityList(onClick = { intensity.value = it })
        Text(text = result.value.toString(), color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
        Calculation(male = male, weight = weight, intensity = intensity, setResult = { result = it })
    }
}

@Composable
fun WeightField(weightInput: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = weightInput,
        onValueChange = onValueChange,
        label = { Text(text = "Enter your weight") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Composable
fun AgeField(ageInput: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = ageInput,
        onValueChange = onValueChange,
        label = { Text(text = "Enter your age") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Composable
fun GenderChoices(male: Boolean, setGenderMale: (Boolean) -> Unit) {
    Column(Modifier.selectableGroup()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = male,
                onClick = { setGenderMale(true) }
            )
            Text(text = "Male")
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = !male,
                onClick = { setGenderMale(false) }
            )
            Text(text = "Female")
        }
    }
}

@Composable
fun IntensityList(onClick: (Float) -> Unit) {
    val expanded = remember { mutableStateOf(false) }
    val selectedText = remember { mutableStateOf("Light") }
    val textFieldSize = remember { mutableStateOf(Size.Zero) }
    val items = listOf("Light", "Usual", "Moderate", "Hard", "Very hard")
    val icon = if (expanded.value) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown

    Column {
        OutlinedTextField(
            value = selectedText.value,
            onValueChange = { selectedText.value = it },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFieldSize.value = coordinates.size.toSize()
                },
            label = { Text("Select intensity") },
            trailingIcon = {
                Icon(icon, contentDescription = "Expand or collapse",
                    Modifier.clickable { expanded.value = !expanded.value }
                )
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
        ) {
            items.forEach { item ->
                DropdownMenuItem(onClick = {
                    selectedText =
                    expanded = false
                    var intensity: Float = when (item) {
                        "Light" -> 1.3f
                        "Usual" -> 1.5f
                        "Moderate" -> 1.7f
                        "Hard" -> 2.0f
                        "Very hard" -> 2.2f
                        else -> 1.3f
                    }
                    onClick(intensity)
                }) {
                    Text(text = item)
                }
            }
        }
    }
}

@Composable
fun Calculation(male: Boolean, weight: Int, age: Int, intensity: Float, setResult: (Int) -> Unit) {
    Button(
        onClick = {
            val result = if (male) {
                (66 + (6.23 * weight) + (12.7 * age) - (6.8 * age)) * intensity
            } else {
                (655 + (4.35 * weight) + (4.7 * age) - (4.7 * age)) * intensity
            }
            setResult(result.toInt())
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Calculate")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Kotlinweek5hrmonTheme {
        Heading("Calories")
    }
}