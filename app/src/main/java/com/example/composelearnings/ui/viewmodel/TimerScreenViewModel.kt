package com.example.composelearnings.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.composelearnings.data.TikTikRepository
import com.example.composelearnings.data.TimerModel
import com.example.composelearnings.utils.addZero
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TimerScreenViewModel(private val tikTikRepository: TikTikRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(TimerModel())
    val uiState = _uiState.asStateFlow()
    private var reachedMaxTime = false
    private var enteredNumbers: MutableList<Int> = mutableListOf()
    private var zeroList: List<Int> = listOf(0, 0, 0, 0, 0, 0)
    private var finalList: List<Int> = listOf()

    fun addedTime(item: Any) {
        if (item is Int && !reachedMaxTime) {

            if (item == 0 && enteredNumbers.isEmpty()) {
                return
            }
            enteredNumbers.add(item)
            finalList = combineLists(zeroList, enteredNumbers)
            if (enteredNumbers.size == 6) {
                reachedMaxTime = true
            }
            _uiState.update { state ->
                state.copy(listOfNumbers = finalList)
            }
        } else {
            if (item == "Back" && enteredNumbers.isNotEmpty()) {
                reachedMaxTime = false
                finalList = listOf(0) + finalList.dropLast(1)
                enteredNumbers.removeAt(enteredNumbers.lastIndex)
                Log.d("eee", "final list: $finalList numberLi $enteredNumbers")
                _uiState.update { value ->
                    value.copy(listOfNumbers = finalList)
                }
            } else if (item == "00" && enteredNumbers.isNotEmpty() && enteredNumbers.size <= 4) {
                reachedMaxTime = false
                enteredNumbers.add(0)
                enteredNumbers.add(0)
                finalList = combineLists(zeroList, enteredNumbers)
                _uiState.update { state ->
                    state.copy(listOfNumbers = finalList)
                }
            }
        }
    }

    private fun combineLists(baseList: List<Int>, newList: List<Int>): List<Int> {
        val startIndex = baseList.size - newList.size
        return baseList.take(startIndex.coerceAtLeast(0)) + newList
    }

    fun findFirstDigitPosition(word: String): Int {
        val regex = Regex("[1-9]")
        val matchResult = regex.find(word)
        val range1 = 0..2
        val range2 = 4..6
        val range3 = 8..10
        val num = when (matchResult?.range?.first) {
            in range3 -> {
                8
            }

            in range2 -> {
                4
            }

            in range1 -> {
                0
            }

            else -> {
                -1
            }
        }
        return num
    }
}