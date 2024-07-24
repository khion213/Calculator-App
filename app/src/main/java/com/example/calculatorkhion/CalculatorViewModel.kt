package com.example.calculatorkhion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import java.util.Stack
import kotlin.math.sqrt
import kotlin.random.Random
import androidx.compose.ui.text.input.TextFieldValue

class CalculatorViewModel : ViewModel() {
    val display = MutableLiveData(TextFieldValue(""))
    val buttonColors = MutableLiveData(
        List(20) { Random.nextInt() }
    )

    var currentInput = ""
        internal set
    private val operations = Stack<String>()
    private val numbers = Stack<Double>()
    private var isSquareRootNext = false

    fun onDigit(digit: Char) {
        if (currentInput.length < 70) {
            currentInput += digit
            updateDisplay(digit.toString())
        }
    }

    fun onOperator(op: Char) {
        if (currentInput.isNotEmpty()) {
            if (isSquareRootNext) {
                val value = sqrt(currentInput.toDouble())
                currentInput = value.toString()
                isSquareRootNext = false
            }
            numbers.push(currentInput.toDouble())
            operations.push(op.toString())
            currentInput = ""
            updateDisplay(" $op ")
        }
    }

    fun onEqual() {
        if (currentInput.isNotEmpty()) {
            if (isSquareRootNext) {
                val value = sqrt(currentInput.toDouble())
                currentInput = value.toString()
                isSquareRootNext = false
            }
            numbers.push(currentInput.toDouble())
            currentInput = ""
            evaluate()
        }
    }

    private fun evaluate() {
        val result = calculate()
        display.value = TextFieldValue(formatResult(result))
        numbers
            .clear()
        operations
            .clear()
    }

    private fun calculate(): Double {
        val tempNumbers = Stack<Double>()
        val tempOperations = Stack<String>()

        tempNumbers.push(numbers.pop())

        while (numbers.isNotEmpty()) {
            val op = operations.pop()
            val num = numbers.pop()

            when (op) {
                "x" -> tempNumbers
                    .push(tempNumbers.pop() * num)
                "÷" -> tempNumbers.push(num / tempNumbers.pop())
                else -> {
                    tempNumbers
                        .push(num)
                    tempOperations.push(op)
                }
            }
        }

        var result = tempNumbers.pop()

        while (tempOperations.isNotEmpty()) {
            val op = tempOperations.pop()
            val num = tempNumbers.pop()

            when (op) {
                "+" -> result += num
                "-" -> result -= num
            }
        }

        return result
    }

    fun onClear() {
        currentInput = ""
        display.value = TextFieldValue("")
        numbers.clear()
        operations.clear()
    }

    fun onBackspace() {
        if (currentInput.isNotEmpty()) {
            currentInput = currentInput.dropLast(1)
            val updatedText = display.value?.text?.dropLast(1) ?: ""
            display.value = TextFieldValue(updatedText)
        } else if (display.value?.text?.lastOrNull() == '√') {
            display.value = TextFieldValue(display.value?.text?.dropLast(1) ?: "")
        }
    }

    fun onBackspaceLongPress() {
        onClear()
    }

    fun shuffleColors() {
        buttonColors.value = List(20) { Random.nextInt() }
    }

    fun onDecimalPoint() {
        if (!currentInput.contains('.')) {
            currentInput += '.'
            updateDisplay(".")
        }
    }

    fun onSquareRoot() {
        isSquareRootNext = true
        updateDisplay("√")
    }

    fun onExit() {
        System.exit(0)
    }

    private fun updateDisplay(newText: String) {
        val currentDisplay = display.value?.text ?: ""
        display.value = TextFieldValue(currentDisplay + newText)
    }

    private fun formatResult(result: Double): String {
        return if (result == result.toLong().toDouble()) {
            result.toLong().toString()
        } else {
            result.toString()
        }
    }
}