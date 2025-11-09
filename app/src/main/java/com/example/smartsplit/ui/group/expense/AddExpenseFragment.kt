package com.example.smartsplit.ui.group.expense

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.smartsplit.R
import com.example.smartsplit.vm.GroupViewModel
//package com.example.smartsplit.ui.group.expense


class AddExpenseFragment : Fragment() {
    companion object {
        private const val ARG_GROUP_ID = "groupId"
        fun newInstance(groupId: String) = AddExpenseFragment().apply {
            arguments = Bundle().apply { putString(ARG_GROUP_ID, groupId) }
        }
    }

    private lateinit var vm: GroupViewModel
    private var groupId = ""
    private lateinit var payerSpinner: Spinner
    private lateinit var etAmount: EditText
    private lateinit var customContainer: LinearLayout
    private var memberIds = listOf<String>()
    private var memberNames = listOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        groupId = requireArguments()?.getString(ARG_GROUP_ID) ?: ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_expense, container, false)
        vm = ViewModelProvider(requireActivity()).get(GroupViewModel::class.java)

        payerSpinner = view.findViewById(R.id.spinnerPayer)
        etAmount = view.findViewById(R.id.etAmount)
        customContainer = view.findViewById(R.id.customContainer)

        vm.members.observe(viewLifecycleOwner) { members ->
            memberIds = members.map { it.memberId }
            memberNames = members.map { it.name }
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, memberNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            payerSpinner.adapter = adapter

            customContainer.removeAllViews()
            for (m in members) {
                val et = EditText(requireContext())
                et.hint = "${m.name} share"
                et.tag = m.memberId
                et.inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
                customContainer.addView(et)
            }
        }
        vm.loadMembers(groupId)

        view.findViewById<Button>(R.id.btnSaveExpense).setOnClickListener {
            val payerIdx = payerSpinner.selectedItemPosition
            if (payerIdx < 0 || payerIdx >= memberIds.size) { Toast.makeText(requireContext(), "Select payer", Toast.LENGTH_SHORT).show(); return@setOnClickListener }
            val payerId = memberIds[payerIdx]
            val amountStr = etAmount.text.toString().trim()
            val amount = amountStr.toDoubleOrNull()
            if (amount == null || amount <= 0.0) { Toast.makeText(requireContext(), "Enter valid amount", Toast.LENGTH_SHORT).show(); return@setOnClickListener }

            val splits = mutableListOf<Pair<String, Double>>()
            if (view.findViewById<RadioButton>(R.id.rbEqual).isChecked) {
                val n = memberIds.size
                val base = kotlin.math.floor((amount / n) * 100.0) / 100.0
                var remainder = kotlin.math.round((amount - base * n) * 100.0) / 100.0
                for (i in memberIds.indices) {
                    val add = if (remainder >= 0.01) { remainder = kotlin.math.round((remainder - 0.01) * 100.0) / 100.0; 0.01 } else 0.0
                    splits.add(Pair(memberIds[i], base + add))
                }
            } else {
                var total = 0.0
                for (i in 0 until customContainer.childCount) {
                    val child = customContainer.getChildAt(i)
                    if (child is EditText) {
                        val v = child.text.toString().trim().toDoubleOrNull() ?: 0.0
                        total += v
                        splits.add(Pair(child.tag as String, v))
                    }
                }
                if (kotlin.math.abs(total - amount) > 0.01) {
                    Toast.makeText(requireContext(), "Custom splits must sum to amount", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
            }

            vm.addExpense(groupId, payerId, amount, splits)
            Toast.makeText(requireContext(), "Expense saved", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }

        return view
    }
}
