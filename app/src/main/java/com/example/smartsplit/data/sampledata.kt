//package com.example.smartsplit.data
//
//import java.util.UUID
//
//fun createSampleData(dao: AppDao) {
//    val gid = "group_trip_1"
//    dao.insertGroup(GroupEntity(groupId = gid, name = "Trip Friends"))
//
//    val members = listOf(
//        MemberEntity(memberId = "A", groupId = gid, name = "Amit"),
//        MemberEntity(memberId = "B", groupId = gid, name = "Bhavya"),
//        MemberEntity(memberId = "C", groupId = gid, name = "Chirag"),
//        MemberEntity(memberId = "D", groupId = gid, name = "Divya")
//    )
//    members.forEach { dao.insertMember(it) }
//
//    // Expense 1: A paid 120 split equally among 4
//    val e1 = ExpenseEntity(
//        expenseId = UUID.randomUUID().toString(),
//        groupId = gid,
//        payerId = "A",
//        amount = 120.0,
//        note = "Dinner"
//    )
//    dao.insertExpense(e1)
//    val share = 30.0
//    for (m in members) {
//        dao.insertExpenseSplit(ExpenseSplitEntity(expenseId = e1.expenseId, memberId = m.memberId, shareAmount = share))
//    }
//
//    // Expense 2: B paid 45 for B and C
//    val e2 = ExpenseEntity(
//        expenseId = UUID.randomUUID().toString(),
//        groupId = gid,
//        payerId = "B",
//        amount = 45.0,
//        note = "Taxi"
//    )
//    dao.insertExpense(e2)
//    dao.insertExpenseSplit(ExpenseSplitEntity(expenseId = e2.expenseId, memberId = "B", shareAmount = 22.5))
//    dao.insertExpenseSplit(ExpenseSplitEntity(expenseId = e2.expenseId, memberId = "C", shareAmount = 22.5))
//
//    // Expense 3: D paid 60 for A and D
//    val e3 = ExpenseEntity(
//        expenseId = UUID.randomUUID().toString(),
//        groupId = gid,
//        payerId = "D",
//        amount = 60.0,
//        note = "Snacks"
//    )
//    dao.insertExpense(e3)
//    dao.insertExpenseSplit(ExpenseSplitEntity(expenseId = e3.expenseId, memberId = "A", shareAmount = 30.0))
//    dao.insertExpenseSplit(ExpenseSplitEntity(expenseId = e3.expenseId, memberId = "D", shareAmount = 30.0))
//}




package com.example.smartsplit.data

import java.util.UUID

suspend fun createSampleData(dao: AppDao) {
    val gid = "group_trip_1"
    dao.insertGroup(GroupEntity(groupId = gid, name = "Trip Friends"))

    val members = listOf(
        MemberEntity(memberId = "A", groupId = gid, name = "Amit"),
        MemberEntity(memberId = "B", groupId = gid, name = "Bhavya"),
        MemberEntity(memberId = "C", groupId = gid, name = "Chirag"),
        MemberEntity(memberId = "D", groupId = gid, name = "Divya")
    )
    members.forEach { dao.insertMember(it) }

    // Example expense (use Long PK auto-generate)
    val e1 = ExpenseEntity(groupId = gid, payerId = "A", amount = 120.0)
    val expenseId = dao.insertExpense(e1) // returns Long
    val splits = listOf(
        ExpenseSplitEntity(expenseId = expenseId, memberId = "A", shareAmount = 30.0),
        ExpenseSplitEntity(expenseId = expenseId, memberId = "B", shareAmount = 30.0),
        ExpenseSplitEntity(expenseId = expenseId, memberId = "C", shareAmount = 30.0),
        ExpenseSplitEntity(expenseId = expenseId, memberId = "D", shareAmount = 30.0),
    )
    dao.insertExpenseSplits(splits)
}
