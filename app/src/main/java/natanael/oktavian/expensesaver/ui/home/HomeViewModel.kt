/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package natanael.oktavian.expensesaver.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import natanael.oktavian.expensesaver.data.Expense
import natanael.oktavian.expensesaver.data.ExpenseWithCategory
import natanael.oktavian.expensesaver.data.ExpensesRepository
import natanael.oktavian.expensesaver.data.FilterData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Date

/**
 * ViewModel to retrieve all items in the Room database.
 */
class HomeViewModel(expensesRepository: ExpensesRepository) : ViewModel() {
    private val _selectedStartDate = MutableStateFlow<Date?>(null)
    private val _selectedEndDate = MutableStateFlow<Date?>(null)

    val homeUiState: StateFlow<HomeUiState> = combine(
        _selectedStartDate,
        _selectedEndDate
    ) { start, end ->
        start to end
    }.flatMapLatest { (start, end) ->
        expensesRepository.getAllExpensesWithCategoryStream(start, end)
    }.map { list ->
        HomeUiState(list)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = HomeUiState()
    )

    companion object {
        private const val TIMEOUT_MILLIS = 1_000L
    }

    fun onDateSelected(date: FilterData) {
        if(date.endDate>=date.startDate){
            _selectedStartDate.value = date.startDate
            _selectedEndDate.value = date.endDate
        }
    }
}

/**
 * Ui State for HomeScreen
 */
data class HomeUiState(val itemList: List<ExpenseWithCategory> = listOf())
