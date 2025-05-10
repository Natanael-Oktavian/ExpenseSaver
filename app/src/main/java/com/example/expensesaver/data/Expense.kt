package com.example.expensesaver.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

/**
 * Entity data class represents a single row in the database.
 */
@Entity(
    tableName = "expenses",
    foreignKeys = [
        ForeignKey(
            entity = ExpenseCategory::class,
            parentColumns = ["categoryId"],
            childColumns = ["expenseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["authorId"])]
)
data class Expense(
    @PrimaryKey
    val expenseId: UUID,
    val categoryId: Int, // foreign key referencing User.userId
    val name: String,
    val amount: Double,
    val createdBy: String,
    val createdDate: Date,
    val isDeleted: Boolean
)
