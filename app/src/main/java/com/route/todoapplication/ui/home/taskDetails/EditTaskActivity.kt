package com.route.todoapplication.ui.home.taskDetails

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.core.content.IntentCompat
import com.route.todoapplication.R
import com.route.todoapplication.base.BaseActivity
import com.route.todoapplication.database.AppDatabase
import com.route.todoapplication.database.model.Task
import com.route.todoapplication.databinding.ActivityEditTaskBinding
import com.route.todoapplication.getFormattedTime
import com.route.todoapplication.ignoreDate
import com.route.todoapplication.ignoreTime
import com.route.todoapplication.ui.home.Constants
import java.util.Calendar

class EditTaskActivity : BaseActivity<ActivityEditTaskBinding>() {
    override fun getLayoutId(): Int = R.layout.activity_edit_task
    private lateinit var intentTask: Task
    private lateinit var newTask: Task
    val dateCalendar: Calendar = Calendar.getInstance()
    val timeCalendar = Calendar.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intentTask =
            IntentCompat.getParcelableExtra(intent, Constants.TASK_KEY, Task::class.java) as Task
        initView()
        setUpToolBar()
        initNewTask()
        onSelectTimeTv()
        onSelectDateTv()
        onSaveClick()


    }

    private fun onSaveClick() {
        binding.content.btnSave.setOnClickListener {
            updateTask()
            finish()
        }
    }

    private fun updateTask() {
        if (!isFieldValid()) {
            return
        }
        newTask.apply {
            title = binding.content.title.text.toString()
            description = binding.content.description.text.toString()
        }
        AppDatabase.getInstance().tasksDao().updateTask(newTask)

    }

    private fun isFieldValid(): Boolean {
        var isValid = true
        if (binding.content.title.text.toString().isBlank()) {
            isValid = false
            binding.content.titleTil.error = "Please enter a title"
        } else {
            binding.content.titleTil.error = null
        }

        return isValid

    }


    private fun onSelectDateTv() {
        binding.content.selectDateTil.setOnClickListener {
            val dialog = DatePickerDialog(this)
            dialog.setOnDateSetListener { datePicker, year, month, day ->
                binding.content.selectDateTv.text = "$day/${month + 1}/$year"
                timeCalendar.ignoreDate()
                timeCalendar.ignoreTime()
                dateCalendar.set(Calendar.YEAR, year)
                dateCalendar.set(Calendar.MONTH, month)
                dateCalendar.set(Calendar.DAY_OF_MONTH, day)
                newTask.date = dateCalendar.timeInMillis

            }
            dialog.show()
        }

    }


    private fun onSelectTimeTv() {
        binding.content.selectTimeTil.setOnClickListener {
            val dialog = TimePickerDialog(
                this,
                { _, hour, minute ->
                    binding.content.selectTimeTv.text = getFormattedTime(hour, minute)
                    timeCalendar.ignoreDate()
                    timeCalendar.ignoreTime()
                    timeCalendar.set(Calendar.HOUR_OF_DAY, hour)
                    timeCalendar.set(Calendar.MINUTE, minute)
                    newTask.time = timeCalendar.timeInMillis


                },
                timeCalendar.get(Calendar.HOUR_OF_DAY),
                timeCalendar.get(Calendar.MINUTE),
                false
            )
            dialog.show()
        }
    }

    private fun initNewTask() {
        newTask = intentTask.copy()
    }

    private fun setUpToolBar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun initView() {
        binding.content.title.setText(intentTask.title)
        binding.content.description.setText(intentTask.description)
        val dateCalender = Calendar.getInstance()
        dateCalender.timeInMillis = intentTask.date!!
        val year = dateCalender.get(Calendar.YEAR)
        val month = dateCalender.get(Calendar.MONTH)
        val day = dateCalender.get(Calendar.DAY_OF_MONTH)
        binding.content.selectDateTv.text = "$day/${month + 1}/$year"
        dateCalender.timeInMillis = intentTask.time!!
        val hour = dateCalender.get(Calendar.HOUR_OF_DAY)
        val minute = dateCalender.get(Calendar.MINUTE)
        binding.content.selectTimeTv.text = getFormattedTime(hour, minute)

    }


}