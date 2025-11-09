//package com.example.smartsplit.data
//
//import androidx.room.Entity
//import androidx.room.PrimaryKey
////
////@Entity
//data class GroupEntity(
//    @PrimaryKey val groupId: String,
//    val name: String,
//    val createdAt: Long = System.currentTimeMillis()
//)
//
//@Entity
//data class MemberEntity(
//    @PrimaryKey val memberId: String,
//    val groupId: String,
//    val name: String
//)
//
//@Entity
//data class ExpenseEntity(
//    @PrimaryKey val expenseId: String,
//    val groupId: String,
//    val payerId: String,
//    val amount: Double,
//    val note: String? = null,
//    val timestamp: Long = System.currentTimeMillis()
//)
//
//@Entity(primaryKeys = ["expenseId", "memberId"])
//data class ExpenseSplitEntity(
//    val expenseId: String,
//    val memberId: String,
//    val shareAmount: Double
//)

package com.example.smartsplit.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GroupEntity(
    @PrimaryKey val groupId: String,
    val name: String
)

@Entity
data class MemberEntity(
    @PrimaryKey val memberId: String,
    val groupId: String,
    val name: String
)

@Entity
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val expenseId: Long = 0,
    val groupId: String,
    val payerId: String,
    val amount: Double
)

@Entity
data class ExpenseSplitEntity(
    @PrimaryKey(autoGenerate = true) val splitId: Long = 0,
    val expenseId: Long,
    val memberId: String,
    val shareAmount: Double
)
