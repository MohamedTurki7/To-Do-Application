package com.route.todoapplication.ui.home

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.route.todoapplication.R
import com.route.todoapplication.database.AppDatabase
import com.route.todoapplication.database.model.Task
import com.route.todoapplication.databinding.FragmentAddTaskBinding
import com.route.todoapplication.dateFormatOnly
import com.route.todoapplication.ignoreDate
import com.route.todoapplication.ignoreTime
import com.route.todoapplication.setCurrentDate
import com.route.todoapplication.setCurrentTime
import com.route.todoapplication.timeFormatOnly
import java.util.Calendar


class AddTaskBottomSheet : BottomSheetDialogFragment() {
    lateinit var binding: FragmentAddTaskBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentAddTaskBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            selectDateTv.setOnClickListener { showDatePicker() }
            selectTimeTv.setOnClickListener { showTimePicker() }
            addTaskBtn.setOnClickListener { createTask() }
        }
    }

    private fun createTask() {
        if (!isValidForm()) return

        // Run the database operation in the background
        AppDatabase.getInstance().tasksDao().createTask(
            Task(
                title = binding.title.text.toString(),
                description = binding.description.text.toString(),
                date = date.timeInMillis,
                time = time.timeInMillis,
            )
        )

        showSuccessDialog()
        tasKAddedListener?.taskAdded()


    }

    private fun showSuccessDialog() {
        val builder = AlertDialog.Builder(context)
            .setMessage("Task added successfully")
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
                dismiss()
            }
            .setCancelable(false)
        builder.show()
    }

    private fun isValidForm(): Boolean {
        var isValid = true
        with(binding) {
            if (title.text.toString().isBlank()) {
                isValid = false
                titleTil.error = "Please enter a title"
            } else {
                titleTil.error = null
            }

            if (description.text.toString().isBlank()) {
                isValid = false
                descriptionTil.error = "Please enter a description"
            } else {
                descriptionTil.error = null
            }

            if (selectDateTv.text.toString().isBlank()) {
                isValid = false
                selectDateTil.error = "Please select a date"
            } else {
                selectDateTil.error = null
            }

            if (selectTimeTv.text.toString().isBlank()) {
                isValid = false
                selectTimeTil.error = "Please select a time"
            } else {
                selectTimeTil.error = null
            }
        }
        return isValid
    }

    fun showTimePicker() {
        val dialog = TimePickerDialog(
            requireContext(), { _, hours, mins ->
                time.setCurrentTime(hours, mins)
                binding.selectTimeTv.text = time.timeFormatOnly()
            },
            Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
            Calendar.getInstance().get(Calendar.MINUTE),
            false
        )
        dialog.show()
    }

    val date: Calendar = Calendar.getInstance().apply { ignoreTime() }
    val time: Calendar = Calendar.getInstance().apply { ignoreDate() }

    private fun showDatePicker() {
        val dialog = DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                date.setCurrentDate(year, month, day)
                binding.selectDateTv.text = date.dateFormatOnly()
            },
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )
        dialog.show()
    }

    var tasKAddedListener: OnTasKAddedListener? = null

    fun interface OnTasKAddedListener {
        fun taskAdded()
    }
}
