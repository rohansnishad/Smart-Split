//package com.example.smartsplit.vm
//
//import android.app.Application
//import androidx.lifecycle.AndroidViewModel
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import com.example.smartsplit.repository.GroupRepository
//import com.example.smartsplit.data.ExpenseEntity
//import com.example.smartsplit.data.ExpenseSplitEntity
//import com.example.smartsplit.data.MemberEntity
//import com.example.smartsplit.data.GroupEntity
//import com.example.smartsplit.util.Expense
//import com.example.smartsplit.util.Member
//import com.example.smartsplit.util.Transaction
//import com.example.smartsplit.util.SettlementCalculator
//
///**
// * AndroidViewModel so we can get application context for the repository.
// * This ViewModel uses synchronous repo calls (demo). Convert to suspend flows for production.
// */
//class GroupViewModel(application: Application) : AndroidViewModel(application) {
//    private val repo = GroupRepository(application.applicationContext)
//
//    // Groups list
//    private val _groups = MutableLiveData<List<GroupEntity>>()
//    val groups: LiveData<List<GroupEntity>> = _groups
//
//    // Members of the selected group
//    private val _members = MutableLiveData<List<MemberEntity>>()
//    val members: LiveData<List<MemberEntity>> = _members
//
//    // Computed settlement transactions
//    private val _transactions = MutableLiveData<List<Transaction>>()
//    val transactions: LiveData<List<Transaction>> = _transactions
//
//    // ===== Repository wrappers =====
//    fun loadGroups() {
//        _groups.value = repo.getAllGroups()
//    }
//
//    fun createSampleData() {
//        repo.createSampleData()
//        loadGroups()
//    }
//
//    fun loadMembers(groupId: String) {
//        _members.value = repo.getMembers(groupId)
//    }
//
//    /**
//     * Add an expense to DB. 'splits' is a list of Pair(memberId, shareAmount).
//     */
//    fun addExpense(
//        groupId: String,
//        payerId: String,
//        amount: Double,
//        splits: List<Pair<String, Double>>
//    ) {
//        val expense = ExpenseEntity(
//            expenseId = java.util.UUID.randomUUID().toString(),
//            groupId = groupId,
//            payerId = payerId,
//            amount = amount,
//            note = null
//        )
//        val splitEntities = splits.map { (memberId, share) ->
//            ExpenseSplitEntity(expenseId = expense.expenseId, memberId = memberId, shareAmount = share)
//        }
//        repo.addExpense(expense, splitEntities)
//    }
//
//    /**
//     * Compute minimal transactions for a group by reading DB, converting to util models,
//     * and calling the SettlementCalculator.
//     */
//    fun calculateSettlements(groupId: String) {
//        // load members and expenses from repo
//        val memberEntities = repo.getMembers(groupId)
//        val expensesWithSplits = repo.getExpensesWithSplits(groupId)
//
//        // convert to util models used by SettlementCalculator
//        val membersUtil = memberEntities.map { Member(it.memberId, it.name) }
//
//        val expensesUtil = expensesWithSplits.map { pair ->
//            val expenseEntity: ExpenseEntity = pair.first
//            val splitsList: List<ExpenseSplitEntity> = pair.second
//            val splitMap: Map<String, Double> = splitsList.associate { it.memberId to it.shareAmount }
//            Expense(expenseEntity.payerId, expenseEntity.amount, splitMap)
//        }
//
//        // call algorithm
//        val result = SettlementCalculator.calculateMinimalTransactions(membersUtil, expensesUtil)
//
//        // (Optional) Map IDs to names for display
//        val idToName = memberEntities.associate { it.memberId to it.name }
//        for (tx in result) {
//            tx.fromName = idToName[tx.fromId]
//            tx.toName = idToName[tx.toId]
//        }
//
//        _transactions.value = result
//    }
//}


package com.example.smartsplit.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.smartsplit.data.*
import com.example.smartsplit.repository.GroupRepository
import kotlinx.coroutines.launch
import com.example.smartsplit.util.SettlementCalculator
import com.example.smartsplit.util.Transaction   // adjust package if different
import kotlinx.coroutines.launch

class GroupViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = GroupRepository(application)
    private val _transactions = MutableLiveData<List<Transaction>>(emptyList())
    val transactions: LiveData<List<Transaction>> = _transactions
    // LiveData backing fields
    private val _groups = MutableLiveData<List<GroupEntity>>(emptyList())
    val groups: LiveData<List<GroupEntity>> = _groups

    private val _members = MutableLiveData<List<MemberEntity>>(emptyList())
    val members: LiveData<List<MemberEntity>> = _members

    private val _expensesWithSplits = MutableLiveData<List<ExpenseWithSplits>>(emptyList())
    val expensesWithSplits: LiveData<List<ExpenseWithSplits>> = _expensesWithSplits

    // ---- load groups ----
    fun calculateSettlements(groupId: String) {
        viewModelScope.launch {
            try {
                // load members & expenses from repo (suspend functions)
                val members = repo.getMembersForGroup(groupId)
                val expensesWithSplits = repo.getExpensesWithSplits(groupId)

                // if SettlementCalculator expects different inputs, adapt here
                val txs = SettlementCalculator.calculateMinimalTransactions(members, expensesWithSplits)

                _transactions.value = txs
            } catch (t: Throwable) {
                // handle/log error; keep LiveData empty or previous value
                // Log.e("GroupViewModel", "calculateSettlements failed", t)
                _transactions.value = emptyList()
            }
        }
    }
    fun loadGroups() {
        viewModelScope.launch {
            try {
                _groups.value = repo.getAllGroups()
            } catch (t: Throwable) {
                // log or handle error
            }
        }
    }

    // ---- create sample data and refresh groups ----
    fun createSampleDataAndRefresh() {
        viewModelScope.launch {
            try {
                repo.createSampleData()
                _groups.value = repo.getAllGroups()
            } catch (t: Throwable) {
                // handle error
            }
        }
    }

    // ---- load members for a group ----
    fun loadMembers(groupId: String) {
        viewModelScope.launch {
            try {
                _members.value = repo.getMembersForGroup(groupId)
            } catch (t: Throwable) {
                // handle error
            }
        }
    }

    // ---- load expenses w/ splits for a group ----
    fun loadExpensesForGroup(groupId: String) {
        viewModelScope.launch {
            try {
                _expensesWithSplits.value = repo.getExpensesWithSplits(groupId)
            } catch (t: Throwable) {
                // handle error
            }
        }
    }

    // ---- add an expense (example) ----
    fun addExpense(expense: ExpenseEntity, splits: List<ExpenseSplitEntity>, onComplete: (Long) -> Unit = {}) {
        viewModelScope.launch {
            try {
                val id = repo.addExpense(expense, splits)
                onComplete(id)
                // optionally refresh list
                _expensesWithSplits.value = repo.getExpensesWithSplits(expense.groupId)
            } catch (t: Throwable) {
                // handle
            }
        }
    }
}
