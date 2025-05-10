package com.example.expensesaver.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

/**
 * Entity data class represents a single row in the database.
 */
@Entity(tableName = "expense_categories")
data class ExpenseCategory(
    @PrimaryKey
    val categoryId: UUID,
    val name: String,
    val createdBy: String,
    val createdDate: Date,
    val isDeleted: Boolean
)
