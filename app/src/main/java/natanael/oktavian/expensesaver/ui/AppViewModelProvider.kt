package natanael.oktavian.expensesaver.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import natanael.oktavian.expensesaver.ExpenseApplication
import natanael.oktavian.expensesaver.ui.expense.ExpenseEditViewModel
import natanael.oktavian.expensesaver.ui.home.HomeViewModel
import natanael.oktavian.expensesaver.ui.expense.ExpenseEntryViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for ItemEditViewModel
        initializer {
            ExpenseEditViewModel(
                this.createSavedStateHandle(),
                expenseApplication().container.expenseRepository,expenseApplication().container.expenseCategoryRepository
            )
        }
        // Initializer for ExpenseEntryViewModel
        initializer {
            ExpenseEntryViewModel(expenseApplication().container.expenseRepository,expenseApplication().container.expenseCategoryRepository)
        }
        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(expenseApplication().container.expenseRepository)
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [ExpenseApplication].
 */
fun CreationExtras.expenseApplication(): ExpenseApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as ExpenseApplication)
