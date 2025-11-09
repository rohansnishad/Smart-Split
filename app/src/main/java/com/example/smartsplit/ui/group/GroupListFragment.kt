package com.example.smartsplit.ui.group

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
import com.example.smartsplit.data.GroupEntity
import com.example.smartsplit.vm.GroupViewModel

class GroupListFragment : Fragment() {
    private lateinit var vm: GroupViewModel
    private lateinit var rv: RecyclerView
    private lateinit var adapter: GroupAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_group_list, container, false)
        vm = ViewModelProvider(requireActivity()).get(GroupViewModel::class.java)

        rv = view.findViewById(R.id.rvGroups)
        rv.layoutManager = LinearLayoutManager(requireContext())
        adapter = GroupAdapter { group -> openGroup(group) }
        rv.adapter = adapter

        view.findViewById<Button>(R.id.btnSettle).setOnClickListener {
            // trigger calculation (ViewModel does the heavy work)
            vm.calculateSettlements(groupId)

            // navigate to settlement fragment — SettlementFragment will observe vm.transactions
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, com.example.smartsplit.ui.group.SettlementFragment())
                .addToBackStack(null)
                .commit()
        }

        vm.groups.observe(viewLifecycleOwner) { adapter.submitList(it) }
        vm.loadGroups()
        return view
    }

    private fun openGroup(group: GroupEntity) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, GroupDetailFragment.newInstance(group.groupId))
            .addToBackStack(null)
            .commit()
    }
}

/* Adapter */
class GroupAdapter(private val onClick: (GroupEntity) -> Unit) : RecyclerView.Adapter<GroupViewHolder>() {
    private val items = mutableListOf<GroupEntity>()
    fun submitList(list: List<GroupEntity>) { items.clear(); items.addAll(list); notifyDataSetChanged() }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return GroupViewHolder(v)
    }
    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val g = items[position]
        holder.itemView.findViewById<android.widget.TextView>(android.R.id.text1).text = g.name
        holder.itemView.setOnClickListener { onClick(g) }
    }
    override fun getItemCount(): Int = items.size
}
class GroupViewHolder(view: View) : RecyclerView.ViewHolder(view)
