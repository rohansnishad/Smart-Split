package com.example.smartsplit.ui.settlement

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartsplit.R
import com.example.smartsplit.util.ExportUtil
import com.example.smartsplit.util.Transaction
import com.example.smartsplit.vm.GroupViewModel

class SettlementFragment : Fragment() {
    companion object { private const val ARG_GROUP_ID = "groupId"
        fun newInstance(groupId: String) = SettlementFragment().apply { arguments = Bundle().apply { putString(ARG_GROUP_ID, groupId) } }
    }
    private lateinit var vm: GroupViewModel
    private lateinit var rvTx: RecyclerView
    private lateinit var adapter: TxAdapter
    private var groupId = ""

    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); groupId = requireArguments()?.getString(ARG_GROUP_ID) ?: "" }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_settlement, container, false)
        vm = ViewModelProvider(requireActivity()).get(GroupViewModel::class.java)

        rvTx = view.findViewById(R.id.rvTransactions)
        rvTx.layoutManager = LinearLayoutManager(requireContext())
        adapter = TxAdapter()
        rvTx.adapter = adapter

        view.findViewById<Button>(R.id.btnShare).setOnClickListener {
            val txs = vm.transactions.value ?: emptyList()
            val csv = ExportUtil.toCsv(txs)
            val intent = Intent(Intent.ACTION_SEND).apply { type = "text/csv"; putExtra(Intent.EXTRA_SUBJECT,"Settlements"); putExtra(Intent.EXTRA_TEXT, csv) }
            startActivity(Intent.createChooser(intent, "Share"))
        }

        vm.transactions.observe(viewLifecycleOwner) { txs ->
            // update your RecyclerView / UI to display transactions
            settlementAdapter.submitList(txs)  // or however you populate UI
        }
        return view
    }
}

/* TxAdapter placed below (simple) */
class TxAdapter : RecyclerView.Adapter<TxViewHolder>() {
    private val items = mutableListOf<Transaction>()
    fun submitList(list: List<Transaction>) { items.clear(); items.addAll(list); notifyDataSetChanged() }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TxViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        return TxViewHolder(v)
    }
    override fun onBindViewHolder(holder: TxViewHolder, position: Int) {
        val t = items[position]
        val from = t.fromName ?: t.fromId; val to = t.toName ?: t.toId
        holder.itemView.findViewById<android.widget.TextView>(R.id.tvTx).text = "$from pays $to ₹${"%.2f".format(t.amount)}"
        holder.itemView.findViewById<android.widget.ImageButton>(R.id.btnCopy).setOnClickListener {
            val txt = "$from pays $to ₹${"%.2f".format(t.amount)}"
            val cb = holder.itemView.context.getSystemService(android.content.ClipboardManager::class.java)
            cb.setPrimaryClip(android.content.ClipData.newPlainText("tx", txt))
            android.widget.Toast.makeText(holder.itemView.context, "Copied", android.widget.Toast.LENGTH_SHORT).show()
        }
        holder.itemView.findViewById<android.widget.ImageButton>(R.id.btnPay).setOnClickListener {
            val uri = android.net.Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa","demo@upi")
                .appendQueryParameter("pn", to)
                .appendQueryParameter("tn","Split payment")
                .appendQueryParameter("am", String.format("%.2f", t.amount))
                .appendQueryParameter("cu","INR").build()
            val intent = Intent(Intent.ACTION_VIEW, uri)
            try { holder.itemView.context.startActivity(intent) } catch (ex: Exception) {
                android.widget.Toast.makeText(holder.itemView.context,"No UPI app", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun getItemCount(): Int = items.size
}
class TxViewHolder(view: View) : RecyclerView.ViewHolder(view)
