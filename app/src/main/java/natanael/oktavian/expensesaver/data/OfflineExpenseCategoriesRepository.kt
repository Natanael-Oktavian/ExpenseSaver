package natanael.oktavian.expensesaver.data

import kotlinx.coroutines.flow.Flow
import java.util.UUID

class OfflineExpenseCategoriesRepository(private val expenseCategoryDao: ExpenseCategoryDao) : ExpenseCategoriesRepository {
    override fun getAllCategoriesStream(): Flow<List<ExpenseCategory>> = expenseCategoryDao.getAllCategories()

    override fun getCategoryByNameStream(name: String) = expenseCategoryDao.getCategoryByName(name)

    override fun getCategoryStream(id: UUID): Flow<ExpenseCategory?> = expenseCategoryDao.getCategory(id)
    override suspend fun insertCategory(item: ExpenseCategory) = expenseCategoryDao.insert(item)

    override suspend fun deleteCategory(item: ExpenseCategory) = expenseCategoryDao.delete(item)
}
