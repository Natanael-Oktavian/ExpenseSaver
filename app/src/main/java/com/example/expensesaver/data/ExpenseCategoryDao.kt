package com.example.expensesaver.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Database access object to access the Inventory database
 */
@Dao
interface ExpenseCategoryDao {

    @Query("SELECT * from expense_categories ORDER BY name ASC")
    fun getAllCategories(): Flow<List<ExpenseCategory>>

    @Query("SELECT * from expense_categories where name =:name")
    fun getCategoryByName(name: String): Flow<ExpenseCategory>

    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Item into the database Room ignores the conflict.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: ExpenseCategory)

    @Delete
    suspend fun delete(item: ExpenseCategory)
}
