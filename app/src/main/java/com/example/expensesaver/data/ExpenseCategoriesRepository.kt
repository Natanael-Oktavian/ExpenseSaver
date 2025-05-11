package com.example.expensesaver.data

import kotlinx.coroutines.flow.Flow
import java.util.UUID

/**
 * Repository that provides insert, update, delete, and retrieve of [ExpenseCategory] from a given data source.
 */
interface ExpenseCategoriesRepository {
    /**
     * Retrieve all the expense from the the given data source.
     */
    fun getAllCategoriesStream(): Flow<List<ExpenseCategory>>

    fun getCategoryByNameStream(name : String): Flow<ExpenseCategory>

    fun getCategoryStream(id: UUID): Flow<ExpenseCategory?>
    /**
     * Insert item in the data source
     */
    suspend fun insertCategory(category: ExpenseCategory)

    /**
     * Delete item from the data source
     */
    suspend fun deleteCategory(category: ExpenseCategory)
}
