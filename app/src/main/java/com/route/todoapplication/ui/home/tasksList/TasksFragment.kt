package com.route.todoapplication.ui.home.tasksList

import TasksListAdapter
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.route.todoapplication.R
import com.route.todoapplication.base.BaseFragment
import com.route.todoapplication.database.AppDatabase
import com.route.todoapplication.database.model.Task
import com.route.todoapplication.databinding.FragmentTasksBinding
import com.route.todoapplication.ignoreTime
import com.route.todoapplication.setCurrentDate
import com.route.todoapplication.ui.home.Constants
import com.route.todoapplication.ui.home.taskDetails.EditTaskActivity
import java.util.Calendar

class TasksFragment : BaseFragment<FragmentTasksBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_tasks
    private val adapter = TasksListAdapter()
    private val selectedDate = Calendar.getInstance().apply {
        ignoreTime()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the adapter to the RecyclerView
        binding.rvTasks.adapter = adapter

        // Handle edit button click in the adapter
        adapter.onEditClickListener = TasksListAdapter.OnItemClickListener { position, task ->
            val intent = Intent(requireContext(), EditTaskActivity::class.java)
            intent.putExtra(Constants.TASK_KEY, task)
            startActivity(intent)
        }

        // Handle delete button click in the adapter
        adapter.onDeleteListener = TasksListAdapter.OnItemClickListener { position, task ->
            deleteTask(task, position)
        }

        // Handle task status change (Done button click)
        adapter.onDoneBtnClick = TasksListAdapter.OnItemClickListener { position, task ->
            task.status = !task.status // Toggle status
            updateTask(task, position)
        }

        // Set current date and handle date change in the calendar
        binding.calendarView.setDateSelected(CalendarDay.today(), true)
        binding.calendarView.setOnDateChangedListener { _, date, _ ->
            selectedDate.setCurrentDate(date.year, date.month - 1, date.day)
            getTasksFromDatabase()
        }
    }

    // Delete task and update adapter
    private fun deleteTask(task: Task, position: Int) {
        Thread {
            AppDatabase.getInstance().tasksDao().deleteTask(task)
            requireActivity().runOnUiThread {
                adapter.removeTaskAt(position) // Remove the task from adapter
            }
        }.start()
    }

    // Update task status and refresh adapter for the changed task
    private fun updateTask(task: Task, position: Int) {
        Thread {
            AppDatabase.getInstance().tasksDao().updateTask(task)
            requireActivity().runOnUiThread {
                adapter.notifyItemChanged(position) // Update only the changed item
            }
        }.start()
    }

    override fun onResume() {
        super.onResume()
        getTasksFromDatabase() // Fetch tasks when fragment resumes
    }

    // Fetch tasks from the database
    fun getTasksFromDatabase() {
        if (isDetached) return

        Thread {
            val tasks = AppDatabase.getInstance().tasksDao()
                .getAllTasks() // Fetch based on the selected date
            requireActivity().runOnUiThread {
                adapter.submitNewList(tasks.toMutableList())
            }
        }.start()
    }
}
