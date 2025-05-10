package com.example.expensesaver.data

import kotlinx.coroutines.flow.Flow

class OfflineExpenseCategoriesRepository(private val expenseCategoryDao: ExpenseCategoryDao) : ExpenseCategoriesRepository {
    override fun getAllCategoriesStream(): Flow<List<ExpenseCategory>> = expenseCategoryDao.getAllCategories()

    override suspend fun insertCategory(item: ExpenseCategory) = expenseCategoryDao.insert(item)

    override suspend fun deleteCategory(item: ExpenseCategory) = expenseCategoryDao.delete(item)
}
