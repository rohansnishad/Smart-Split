//package com.example.smartsplit.repository
//
//import android.content.Context
//import com.example.smartsplit.AppDatabase
//import com.example.smartsplit.data.*
//
///**
// * Lightweight repository for demo: synchronous Room access (allowMainThreadQueries used).
// * For production, convert methods to suspend and use coroutines.
// */
//class GroupRepository(context: Context) {
//    private val dao: AppDao = AppDatabase.getInstance(context).appDao()
//
//    // Sample data
//    fun createSampleData() {
//        com.example.smartsplit.data.createSampleData(dao)
//    }
//
//    // Groups
//    fun getAllGroups(): List<GroupEntity> = dao.getAllGroups()
//
//    // Members
//    fun getMembers(groupId: String): List<MemberEntity> = dao.getMembersForGroup(groupId)
//
//    // Expenses with splits (pairs)
//    fun getExpensesWithSplits(groupId: String): List<Pair<ExpenseEntity, List<ExpenseSplitEntity>>> =
//        dao.getExpensesWithSplits(groupId)
//
//    // Insert expense and its splits
//    fun addExpense(expense: ExpenseEntity, splits: List<ExpenseSplitEntity>) {
//        dao.insertExpense(expense)
//        splits.forEach { dao.insertExpenseSplit(it) }
//    }
//}
//
//package com.example.smartsplit.repository
//
//import android.content.Context
//import com.example.smartsplit.AppDatabase
//import com.example.smartsplit.data.ExpenseEntity
//import com.example.smartsplit.data.ExpenseSplitEntity
//import com.example.smartsplit.data.GroupEntity
//import com.example.smartsplit.data.MemberEntity
//import com.example.smartsplit.data.AppDao
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//
//class GroupRepository(context: Context) {
//    private val dao: AppDao = AppDatabase.getInstance(context).appDao()
//
//    // sample data (suspend)
//    suspend fun createSampleData() = withContext(Dispatchers.IO) {
//        com.example.smartsplit.data.createSampleData(dao)
//    }
//
//    // groups
//    suspend fun getAllGroups(): List<GroupEntity> = withContext(Dispatchers.IO) {
//        dao.getAllGroups()
//    }
//
//    // members
//    suspend fun getMembers(groupId: String): List<MemberEntity> = withContext(Dispatchers.IO) {
//        dao.getMembersForGroup(groupId)
//    }
//
//    // expenses w/ splits
//    suspend fun getExpensesWithSplits(groupId: String): List<Pair<ExpenseEntity, List<ExpenseSplitEntity>>> =
//        withContext(Dispatchers.IO) {
//            dao.getExpensesWithSplits(groupId)
//        }
//
//    // add expense + splits
//    suspend fun addExpense(expense: ExpenseEntity, splits: List<ExpenseSplitEntity>) = withContext(Dispatchers.IO) {
//        dao.insertExpense(expense)
//        splits.forEach { dao.insertExpenseSplit(it) }
//    }
//}


package com.example.smartsplit.repository

import android.content.Context
import com.example.smartsplit.AppDatabase
import com.example.smartsplit.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GroupRepository(context: Context) {
    private val dao = AppDatabase.getInstance(context).appDao()

    // ---- Groups ----
    suspend fun getAllGroups(): List<GroupEntity> = withContext(Dispatchers.IO) {
        dao.getAllGroups()
    }

    suspend fun createSampleData() = withContext(Dispatchers.IO) {
        // delegate to your sample data function which should be suspend (see earlier advice)
        createSampleData(dao)
    }

    // ---- Members ----
    suspend fun getMembersForGroup(groupId: String): List<MemberEntity> = withContext(Dispatchers.IO) {
        dao.getMembersForGroup(groupId)
    }

    // ---- Expenses with splits ----
    suspend fun getExpensesWithSplits(groupId: String): List<ExpenseWithSplits> = withContext(Dispatchers.IO) {
        dao.getExpensesWithSplits(groupId)
    }

    // ---- Add expense + splits ----
    suspend fun addExpense(expense: ExpenseEntity, splits: List<ExpenseSplitEntity>): Long = withContext(Dispatchers.IO) {
        val id = dao.insertExpense(expense)
        if (splits.isNotEmpty()) {
            val prepared = splits.map { it.copy(expenseId = id) }
            dao.insertExpenseSplits(prepared)
        }
        id
    }
}

