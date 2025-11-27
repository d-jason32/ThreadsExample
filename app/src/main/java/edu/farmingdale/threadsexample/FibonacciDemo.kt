package edu.farmingdale.threadsexample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.Locale
import kotlin.text.format

@Composable
fun FibonacciDemoNoBgThrd() {
    var answer by remember { mutableStateOf("") }
    var textInput by remember { mutableStateOf("40") }

    Column {
        Row {
            TextField(
                value = textInput,
                onValueChange = { textInput = it },
                label = { Text("Number?") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )
            Button(onClick = {
                val num = textInput.toLongOrNull() ?: 0
                val fibNumber = fibonacci(num)
                answer = NumberFormat.getNumberInstance(Locale.US).format(fibNumber)
            }) {
                Text("Fibonacci")
            }
        }

        Text("Result: $answer")
    }
}

fun fibonacci(n: Long): Long {
    return if (n <= 1) n else fibonacci(n - 1) + fibonacci(n - 2)
}

/**
 * Fibonacci using Coroutine from lecture.
 */
@Composable
fun FibonacciDemoWithCoroutine() {
    var answer by remember { mutableStateOf("") }
    var textInput by remember { mutableStateOf("10") }
    var fibonacciJob: Job? by remember { mutableStateOf(null) }
    val coroutineScope = rememberCoroutineScope()

    Column(

        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )        {


            TextField(
                value = textInput,
                onValueChange = { textInput = it },
                label = { Text("Enter a number") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Button(onClick = {
                val num = textInput.toLongOrNull() ?: 0L
                fibonacciJob = coroutineScope.launch {
                    val fibNumber = withContext(Dispatchers.Default) {
                        fibonacciSuspend(num)
                    }
                    answer = NumberFormat.getNumberInstance(Locale.US).format(fibNumber)
                }
            }) {
                Text("Start")
            }

            Button(onClick = { fibonacciJob?.cancel() }) {
                Text("Cancel")
            }


        Text("Result: $answer")
    }
}

suspend fun fibonacciSuspend(n: Long): Long {
    delay(10)
    return if (n <= 1) n else fibonacciSuspend(n - 1) +
            fibonacciSuspend(n - 2)
}