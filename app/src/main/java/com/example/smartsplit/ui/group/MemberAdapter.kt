package com.example.smartsplit.ui.group

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.smartsplit.R

class MemberAdapter : RecyclerView.Adapter<MemberViewHolder>() {
    private val items = mutableListOf<Pair<String, Double>>()
    fun submitList(list: List<Pair<String, Double>>) { items.clear(); items.addAll(list); notifyDataSetChanged() }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_member, parent, false)
        return MemberViewHolder(v)
    }
    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        val (name, net) = items[position]
        holder.itemView.findViewById<android.widget.TextView>(R.id.tvName).text = name
        val tvNet = holder.itemView.findViewById<android.widget.TextView>(R.id.tvNet)
        val formatted = "₹${"%.2f".format(net)}"
        when {
            net > 0.0 -> { tvNet.text = "+$formatted"; tvNet.setTextColor(ContextCompat.getColor(holder.itemView.context, android.R.color.holo_green_dark)) }
            net < 0.0 -> { tvNet.text = formatted; tvNet.setTextColor(ContextCompat.getColor(holder.itemView.context, android.R.color.holo_red_dark)) }
            else -> { tvNet.text = "₹0.00"; tvNet.setTextColor(ContextCompat.getColor(holder.itemView.context, android.R.color.darker_gray)) }
        }
    }
    override fun getItemCount(): Int = items.size
}
class MemberViewHolder(view: View) : RecyclerView.ViewHolder(view)
