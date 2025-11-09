package com.example.smartsplit.util

object ExportUtil {
    fun toCsv(txs: List<Transaction>): String {
        val sb = StringBuilder()
        sb.append("From,To,Amount\n")
        for (t in txs) {
            val from = t.fromName ?: t.fromId
            val to = t.toName ?: t.toId
            sb.append("$from,$to,${"%.2f".format(t.amount)}\n")
        }
        return sb.toString()
    }
}
