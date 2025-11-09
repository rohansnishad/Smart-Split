//package com.example.smartsplit.util
//
//import org.junit.Assert.assertEquals
//import org.junit.Test
//
//class SettlementCalculatorTest {
//
//    @Test
//    fun simpleEqualSplit_twoTxs() {
//        val members = listOf(
//            Member("A", "Amit"),
//            Member("B", "Bhavya"),
//            Member("C", "Chirag"),
//            Member("D", "Divya")
//        )
//
//        val expenses = listOf(
//            Expense("A", 120.0, mapOf("A" to 30.0, "B" to 30.0, "C" to 30.0, "D" to 30.0)),
//            Expense("B", 45.0, mapOf("B" to 22.5, "C" to 22.5)),
//            Expense("D", 60.0, mapOf("A" to 30.0, "D" to 30.0))
//        )
//
//        val txs = SettlementCalculator.calculateMinimalTransactions(members, expenses)
//
//        // Expect 2 transactions and totals equal
//        assertEquals(2, txs.size)
//
//        // Sum of amounts paid should equal sum of positive nets
//        val total = txs.sumOf { it.amount }
//        val nets = run {
//            val map = members.associate { it.id to 0.0 }.toMutableMap()
//            for (e in expenses) {
//                map[e.payerId] = map.getValue(e.payerId) + e.amount
//                for ((id, s) in e.splits) map[id] = map.getValue(id) - s
//            }
//            map.values.filter { it > 0 }.sum()
//        }
//        // allow small floating diff
//        assertEquals(String.format("%.2f", nets), String.format("%.2f", total))
//    }
//
//    @Test
//    fun roundingEdge_smallResidualHandled() {
//        val members = listOf(Member("A","A"), Member("B","B"), Member("C","C"))
//        val expenses = listOf(Expense("A", 100.0, mapOf("A" to 33.33, "B" to 33.33, "C" to 33.34)))
//        val txs = SettlementCalculator.calculateMinimalTransactions(members, expenses)
//        // everything should settle (sum amounts equals positive nets)
//        val totalTx = txs.sumOf { it.amount }
//        val netsPositive = members.map { it.id }.map { id ->
//            var net = 0.0
//            for (e in expenses) {
//                if (e.payerId == id) net += e.amount
//                net -= e.splits.getOrDefault(id, 0.0)
//            }
//            if (net > 0) net else 0.0
//        }.sum()
//        assertEquals(String.format("%.2f", netsPositive), String.format("%.2f", totalTx))
//    }
//}
