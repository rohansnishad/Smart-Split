//package com.example.smartsplit.ui.group
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.ViewModelProvider
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.smartsplit.R
//import com.example.smartsplit.vm.GroupViewModel
//
//class GroupDetailFragment : Fragment() {
//    companion object {
//        private const val ARG_GROUP_ID = "groupId"
//        fun newInstance(groupId: String) = GroupDetailFragment().apply {
//            arguments = Bundle().apply { putString(ARG_GROUP_ID, groupId) }
//        }
//    }
//
//    private lateinit var groupId: String   // ✅ add this
//    private lateinit var vm: GroupViewModel
//    private lateinit var rvMembers: RecyclerView
//    private lateinit var memberAdapter: MemberAdapter
//    private var groupId: String = ""
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        groupId = requireArguments()?.getString(ARG_GROUP_ID) ?: ""
//    }
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        val view = inflater.inflate(R.layout.fragment_group_detail, container, false)
//        vm = ViewModelProvider(requireActivity()).get(GroupViewModel::class.java)
//
//        rvMembers = view.findViewById(R.id.rvMembers)
//        rvMembers.layoutManager = LinearLayoutManager(requireContext())
//        memberAdapter = MemberAdapter()
//        rvMembers.adapter = memberAdapter
//
//        view.findViewById<Button>(R.id.btnAddExpense).setOnClickListener {
//            parentFragmentManager.beginTransaction()
//                .replace(R.id.container, com.example.smartsplit.ui.expense.AddExpenseFragment.newInstance(groupId))
//                .addToBackStack(null)
//                .commit()
//        }
//
//        view.findViewById<Button>(R.id.btnSettle).setOnClickListener {
//            vm.calculateSettlements(groupId)
//            parentFragmentManager.beginTransaction()
//                .replace(R.id.container, com.example.smartsplit.ui.settlement.SettlementFragment.newInstance(groupId))
//                .addToBackStack(null)
//                .commit()
//        }
//
//        vm.members.observe(viewLifecycleOwner) { members ->
//            // show names; net will be 0 until calculated (you can enhance later)
//            memberAdapter.submitList(members.map { Pair(it.name, 0.0) })
//        }
//        vm.loadMembers(groupId)
//        return view
//    }
//}


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
import com.example.smartsplit.vm.GroupViewModel
import com.example.smartsplit.ui.group.MemberAdapter

// NOTE: I intentionally DON'T import AddExpenseFragment/SettlementFragment here.
// If your project package for them matches the lines below, you can uncomment imports.
// import com.example.smartsplit.ui.group.expense.AddExpenseFragment
// import com.example.smartsplit.ui.group.settlement.SettlementFragment

class GroupDetailFragment : Fragment() {

    companion object {
        private const val ARG_GROUP_ID = "groupId"
        fun newInstance(groupId: String) = GroupDetailFragment().apply {
            arguments = Bundle().apply { putString(ARG_GROUP_ID, groupId) }
        }
    }

    private var groupId: String = ""
    private lateinit var vm: GroupViewModel
    private lateinit var rvMembers: RecyclerView
    private lateinit var memberAdapter: MemberAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        groupId = arguments?.getString(ARG_GROUP_ID) ?: ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_group_detail, container, false)
        vm = ViewModelProvider(requireActivity()).get(GroupViewModel::class.java)

        rvMembers = view.findViewById(R.id.rvMembers)
        rvMembers.layoutManager = LinearLayoutManager(requireContext())
        memberAdapter = MemberAdapter()
        rvMembers.adapter = memberAdapter

        view.findViewById<Button>(R.id.btnAddExpense).setOnClickListener {
            // Use fully-qualified class name to avoid import mismatch
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, com.example.smartsplit.ui.group.expense.AddExpenseFragment.newInstance(groupId))
                .addToBackStack(null)
                .commit()
        }

        view.findViewById<Button>(R.id.btnSettle).setOnClickListener {
            vm.calculateSettlements(groupId)
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, com.example.smartsplit.ui.group.settlement.SettlementFragment.newInstance(groupId))
                .addToBackStack(null)
                .commit()
        }

        vm.members.observe(viewLifecycleOwner) { members ->
            memberAdapter.submitList(members.map { Pair(it.name, 0.0) })
        }

        vm.loadMembers(groupId)
        return view
    }
}
