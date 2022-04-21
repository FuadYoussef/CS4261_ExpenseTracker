package com.mas.expensetracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment

class CustomDialog : DialogFragment() {

    private lateinit var btnManageGroup: Button
    private lateinit var btnPrintAudit: Button
    private lateinit var btnCancel: Button

    interface OnInputListener {
        fun manageGroup()
        fun printAudit()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.alert_dialog_box, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnManageGroup = view.findViewById(R.id.manage_participants)
        btnPrintAudit = view.findViewById(R.id.print_audit)
        btnManageGroup.setOnClickListener {

            (activity as OnInputListener).manageGroup()

            dialog?.dismiss()
        }

        btnPrintAudit.setOnClickListener {

            (activity as OnInputListener).printAudit()

            dialog?.dismiss()
        }

        btnCancel = view.findViewById(R.id.cancel_button)
        btnCancel.setOnClickListener { dialog?.dismiss() }
    }
}