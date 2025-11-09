//package com.example.smartsplit.data
//
//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//
//@Dao
//interface AppDao {
//
//    // Groups
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertGroup(group: GroupEntity)
//
//    @Query("SELECT * FROM GroupEntity")
//    fun getAllGroups(): List<GroupEntity>
//
//    // Members
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertMember(member: MemberEntity)
//
//    @Query("SELECT * FROM MemberEntity WHERE groupId = :groupId")
//    fun getMembersForGroup(groupId: String): List<MemberEntity>
//
//    // Expenses
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertExpense(expense: ExpenseEntity)
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertExpenseSplit(split: ExpenseSplitEntity)
//
//    @Query("SELECT * FROM ExpenseEntity WHERE groupId = :groupId")
//    fun getExpensesForGroup(groupId: String): List<ExpenseEntity>
//
//    @Query("SELECT * FROM ExpenseSplitEntity WHERE expenseId = :expenseId")
//    fun getSplitsForExpense(expenseId: String): List<ExpenseSplitEntity>
//
//    // Helper to gather expenses with their splits (simple impl)
//    fun getExpensesWithSplits(groupId: String): List<Pair<ExpenseEntity, List<ExpenseSplitEntity>>> {
//        val expenses = getExpensesForGroup(groupId)
//        val out = mutableListOf<Pair<ExpenseEntity, List<ExpenseSplitEntity>>>()
//        for (e in expenses) {
//            val splits = getSplitsForExpense(e.expenseId)
//            out.add(Pair(e, splits))
//        }
//        return out
//    }
//}

package com.example.smartsplit.data

import androidx.room.*

@Dao
interface AppDao {

    // GROUPS
    @Query("SELECT * FROM GroupEntity")
    suspend fun getAllGroups(): List<GroupEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(group: GroupEntity): Long

    @Delete
    suspend fun deleteGroup(group: GroupEntity)

    // MEMBERS
    @Query("SELECT * FROM MemberEntity WHERE groupId = :groupId")
    suspend fun getMembersForGroup(groupId: String): List<MemberEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: MemberEntity): Long

    // EXPENSES and SPLITS
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpenseSplits(splits: List<ExpenseSplitEntity>)

    @Transaction
    @Query("SELECT * FROM ExpenseEntity WHERE groupId = :groupId")
    suspend fun getExpensesWithSplits(groupId: String): List<ExpenseWithSplits>

    // Example of atomic operation
    @Transaction
    suspend fun insertExpenseWithSplits(expense: ExpenseEntity, splits: List<ExpenseSplitEntity>) {
        val id = insertExpense(expense)        // id is Long
        if (splits.isNotEmpty()) {
            insertExpenseSplits(splits.map { it.copy(expenseId = id) })
        }
    }
}
