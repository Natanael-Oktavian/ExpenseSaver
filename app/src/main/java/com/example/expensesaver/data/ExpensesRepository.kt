package com.example.expensesaver.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [Expense] from a given data source.
 */
interface ExpensesRepository {

    fun getAllExpensesWithCategoryStream(): Flow<List<ExpenseWithCategory>>

    /**
     * Retrieve all the expense from the the given data source.
     */
    fun getAllExpensesStream(): Flow<List<Expense>>

    /**
     * Retrieve an expense from the given data source that matches with the [id].
     */
    fun getExpenseStream(id: Int): Flow<Expense?>

    /**
     * Insert item in the data source
     */
    suspend fun insertItem(expense: Expense)

    suspend fun insertCategoryWithExpense(category: ExpenseCategory, expense: Expense)

    /**
     * Delete item from the data source
     */
    suspend fun deleteItem(expense: Expense)

    /**
     * Update item in the data source
     */
    suspend fun updateItem(expense: Expense)
}
