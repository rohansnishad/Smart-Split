package com.example.smartsplit.data

import androidx.room.Embedded
import androidx.room.Relation

// This is a "relation" data class, not an Entity.
data class ExpenseWithSplits(
    @Embedded val expense: ExpenseEntity,

    @Relation(
        parentColumn = "expenseId",
        entityColumn = "expenseId"
    )
    val splits: List<ExpenseSplitEntity>
)
