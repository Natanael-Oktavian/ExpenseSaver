package com.example.expensesaver.data

import kotlinx.coroutines.flow.Flow
import java.util.UUID

class OfflineExpensesRepository(private val itemDao: ExpenseDao) : ExpensesRepository {
    override fun getAllExpensesWithCategoryStream(): Flow<List<ExpenseWithCategory>> = itemDao.getAllExpensesWithCategory()

    override fun getAllExpensesStream(): Flow<List<Expense>> = itemDao.getAllExpenses()

    override fun getExpenseStream(id: UUID): Flow<Expense?> = itemDao.getExpense(id)

    override suspend fun insertItem(expense: Expense) = itemDao.insert(expense)

    override suspend fun insertCategoryWithExpense(category: ExpenseCategory, expense: Expense) = itemDao.insertCategoryWithExpense(category,expense)

    override suspend fun deleteItem(expense: Expense) = itemDao.delete(expense)

    override suspend fun updateItem(expense: Expense) = itemDao.update(expense)
}
