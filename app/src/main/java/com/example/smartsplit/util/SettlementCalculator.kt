package com.example.smartsplit.util

import kotlin.math.roundToLong

// Simple models for algorithm
data class Transaction(val fromId: String?, val toId: String?, val amount: Double)

data class Member(val id: String, val name: String)
data class Expense(val payerId: String, val amount: Double, val splits: Map<String, Double>)
//data class Transaction(
//    val fromId: String,
//    val toId: String,
//    val amount: Double,
//    var fromName: String? = null,
//    var toName: String? = null
//)

/**
 * SettlementCalculator
 * - Greedy Min-Cash-Flow: repeatedly match largest creditor with largest debtor.
 * - Rounds to 2 decimals to avoid tiny floating errors.
 */
object SettlementCalculator {

    /**
     * members: list of Member objects (ids must match splits/payers)
     * expenses: list of Expense objects (each has payerId, amount, and map of memberId->shareAmount)
     *
     * Returns list of Transaction(from -> to, amount)
     */
    fun calculateMinimalTransactions(members: List<Member>, expenses: List<Expense>): List<Transaction> {
        // Build net balances map (memberId -> net)
        val nets = HashMap<String, Double>()
        members.forEach { nets[it.id] = 0.0 }

        // payer gets +amount, each participant owes their share (-share)
        for (exp in expenses) {
            // Add payer amount
            val prev = nets[exp.payerId] ?: 0.0
            nets[exp.payerId] = prev + exp.amount

            // Subtract each participant's share
            for ((memberId, share) in exp.splits) {
                val prevShare = nets[memberId] ?: 0.0
                nets[memberId] = prevShare - share
            }
        }

        // Priority queues: creditors descending, debtors ascending (negative numbers)
        val creditors = java.util.PriorityQueue<Pair<String, Double>>(compareByDescending { it.second })
        val debtors = java.util.PriorityQueue<Pair<String, Double>>(compareBy { it.second })

        // Normalize rounding to 2 decimals and populate queues
        for ((id, net) in nets) {
            val rounded = round2(net)
            if (rounded > 0.0) creditors.add(Pair(id, rounded))
            else if (rounded < 0.0) debtors.add(Pair(id, rounded))
        }

        val result = mutableListOf<Transaction>()

        while (creditors.isNotEmpty() && debtors.isNotEmpty()) {
            val (credId, credAmt) = creditors.poll()
            val (debId, debAmt) = debtors.poll() // debAmt is negative

            val settleAmt = minOf(credAmt, -debAmt)
            val settled = round2(settleAmt)
            result.add(Transaction(fromId = debId, toId = credId, amount = settled))

            val credRem = round2(credAmt - settleAmt)
            val debRem = round2(debAmt + settleAmt) // still negative or zero

            if (credRem > 0.0) creditors.add(Pair(credId, credRem))
            if (debRem < 0.0) debtors.add(Pair(debId, debRem))
        }

        return result
    }

    private fun round2(value: Double): Double =
        (value * 100.0).roundToLong() / 100.0
}
