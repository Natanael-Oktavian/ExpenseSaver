package natanael.oktavian.expensesaver.ui.expense

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import natanael.oktavian.expensesaver.data.ExpenseCategoriesRepository
import natanael.oktavian.expensesaver.data.ExpenseCategory
import natanael.oktavian.expensesaver.data.ExpensesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import java.util.UUID

/**
 * ViewModel to retrieve and update an item from the [ExpensesRepository]'s data source.
 */
class ExpenseEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val expenseRepository: ExpensesRepository,
    private val expenseCategoryRepository: ExpenseCategoriesRepository
) : ViewModel() {

    /**
     * Holds current item ui state
     */
    var itemUiState by mutableStateOf(ItemUiState())
        private set

    private val itemId: UUID = UUID.fromString(
        checkNotNull(savedStateHandle[ExpenseEditDestination.itemIdArg])
    )
    init {
        viewModelScope.launch {
            itemUiState = expenseRepository.getExpenseStream(itemId)
                .filterNotNull()
                .first()
                .toItemUiState(true)

            var exisingCategory = expenseCategoryRepository.getCategoryStream(itemUiState.itemDetails.categoryId).firstOrNull()
            if (exisingCategory != null) {
                itemUiState = itemUiState.copy(
                    itemDetails = itemUiState.itemDetails.copy(
                        categoryName = exisingCategory.name
                    )
                )
            }
        }
    }

    /**
     * Update the item in the [ExpensesRepository]'s data source
     */
    suspend fun updateItem() {
        if (validateInput(itemUiState.itemDetails)) {
            expenseRepository.updateItem(itemUiState.itemDetails.toItem())
        }
    }

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
                expenseRepository.updateItem(expense)
            }
        }
    }

    /**
     * Updates the [itemUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(itemDetails: ItemDetails) {
        itemUiState =
            ItemUiState(itemDetails = itemDetails, isEntryValid = validateInput(itemDetails))
    }

    private fun validateInput(uiState: ItemDetails = itemUiState.itemDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && amount.isNotBlank() && categoryName.isNotBlank()
        }
    }
}
