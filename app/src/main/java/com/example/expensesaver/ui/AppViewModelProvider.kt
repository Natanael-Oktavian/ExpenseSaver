package com.example.expensesaver.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.expensesaver.ExpenseApplication
import com.example.expensesaver.ui.home.HomeViewModel
import com.example.expensesaver.ui.expense.ExpenseEntryViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {

        // Initializer for ItemEntryViewModel
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
