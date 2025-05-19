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

package natanael.oktavian.expensesaver.ui.expense

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import natanael.oktavian.expensesaver.data.Expense
import natanael.oktavian.expensesaver.data.ExpenseCategoriesRepository
import natanael.oktavian.expensesaver.data.ExpenseCategory
import natanael.oktavian.expensesaver.data.ExpensesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.Currency
import java.util.Date
import java.util.Locale
import java.util.UUID

/**
 * ViewModel to validate and insert items in the Room database.
 */
class ExpenseEntryViewModel(private val expenseRepository: ExpensesRepository,private val expenseCategoryRepository: ExpenseCategoriesRepository) : ViewModel() {

    /**
     * Holds current item ui state
     */
    var itemUiState by mutableStateOf(ItemUiState())
        private set

    /**
     * Updates the [itemUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(itemDetails: ItemDetails) {
        itemUiState =
            ItemUiState(itemDetails = itemDetails, isEntryValid = validateInput(itemDetails))
    }

    /**
     * Inserts an [Expense] in the Room database
     */
    suspend fun saveItem() {
        if (validateInput()) {
            withContext(Dispatchers.IO) {
                var exisingCategory = expenseCategoryRepository.getCategoryByNameStream(itemUiState.itemDetails.categoryName).firstOrNull()

                val categoryId = exisingCategory?.categoryId ?: UUID.randomUUID()
                if(exisingCategory==null) {
                    val expenseCategory = ExpenseCategory(
                        categoryId = categoryId,
                        name = itemUiState.itemDetails.categoryName,
                        createdBy = "System",
                        createdDate = Date(),
                        isDeleted = false
                    )
                    expenseCategoryRepository.insertCategory(expenseCategory)
                }
                val expense = itemUiState.itemDetails.toItem(categoryId)
                expenseRepository.insertItem(expense)
            }
        }
    }

    private fun validateInput(uiState: ItemDetails = itemUiState.itemDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && amount.isNotBlank() && categoryName.isNotBlank()
        }
    }
}

/**
 * Represents Ui State for an Item.
 */
data class ItemUiState(
    val itemDetails: ItemDetails = ItemDetails(),
    val isEntryValid: Boolean = false
)

data class ItemDetails(
    val expenseId: UUID = UUID.randomUUID(),
    val categoryId: UUID= UUID.randomUUID(),
    val name: String="",
    val amount: String="",
    val createdBy: String="",
    val createdDate: Date=Date(),
    val isDeleted: Boolean=false,
    var categoryName: String=""
)

/**
 * Extension function to convert [ItemUiState] to [Expense]. If the value of [ItemDetails.amount] is
 * not a valid [Double], then the price will be set to 0.0. Similarly if the value of
 * [ItemUiState] is not a valid [Int], then the quantity will be set to 0
 */
fun ItemDetails.toItem(categoryId:UUID): Expense = Expense(
    expenseId = expenseId,
    categoryId = categoryId,
    name = name,
    amount = amount.toDoubleOrNull() ?: 0.0,
    createdBy = createdBy,
    createdDate = createdDate,
    isDeleted = isDeleted,
)

fun ItemDetails.toItem(): Expense = Expense(
    expenseId = expenseId,
    categoryId = categoryId,
    name = name,
    amount = amount.toDoubleOrNull() ?: 0.0,
    createdBy = createdBy,
    createdDate = createdDate,
    isDeleted = isDeleted,
)

fun Expense.formatedPrice(): String {
    val locale = Locale("id", "ID")
    val formatter = NumberFormat.getCurrencyInstance(locale).apply {
        maximumFractionDigits = 0 // removes decimals
    }
    return formatter.format(amount).replace("Rp", "")
}

/**
 * Extension function to convert [Expense] to [ItemUiState]
 */
fun Expense.toItemUiState(isEntryValid: Boolean = false): ItemUiState = ItemUiState(
    itemDetails = this.toItemDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [Expense] to [ItemDetails]
 */
fun Expense.toItemDetails(): ItemDetails = ItemDetails(
    expenseId = expenseId,
    categoryId = categoryId,
    name = name,
    amount = amount.toString(),
    createdBy = createdBy,
    createdDate = createdDate,
    isDeleted = isDeleted,
)
