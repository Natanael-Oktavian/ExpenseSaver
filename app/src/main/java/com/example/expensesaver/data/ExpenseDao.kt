package com.example.expensesaver.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

data class ExpenseWithCategory(
    @Embedded val expense: Expense,

    @Relation(
        parentColumn = "categoryId",
        entityColumn = "categoryId"
    )
    val category: ExpenseCategory
)

/**
 * Database access object to access the Inventory database
 */
@Dao
interface ExpenseDao {

    @Query("SELECT * from expenses ORDER BY createdDate ASC")
    fun getAllExpensesWithCategory(): Flow<List<ExpenseWithCategory>>

    @Query("SELECT * from expenses ORDER BY createdDate ASC")
    fun getAllExpenses(): Flow<List<Expense>>

    @Query("SELECT * from expenses WHERE expenseId = :expenseId")
    fun getExpense(expenseId: Int): Flow<Expense>

    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Item into the database Room ignores the conflict.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Expense)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategory(item: ExpenseCategory)

    @Transaction
    suspend fun insertCategoryWithExpense(category: ExpenseCategory, expense: Expense) {
        insertCategory(category)
        insert(expense)
    }

    @Update
    suspend fun update(item: Expense)

    @Delete
    suspend fun delete(item: Expense)
}
