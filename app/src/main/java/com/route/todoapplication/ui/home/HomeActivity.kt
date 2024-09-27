package com.route.todoapplication.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.route.todoapplication.R
import com.route.todoapplication.base.BaseActivity
import com.route.todoapplication.databinding.ActivityHomeBinding
import com.route.todoapplication.ui.home.settings.SettingsFragment
import com.route.todoapplication.ui.home.tasksList.TasksFragment

class HomeActivity : BaseActivity<ActivityHomeBinding>() {
    var tasksFragment: TasksFragment? = null
    override fun getLayoutId(): Int = R.layout.activity_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.bottomNavigation.setOnItemSelectedListener { item ->

            when (item.itemId) {
                R.id.tasks_navigation -> {
                    tasksFragment = TasksFragment()
                    showFragment(tasksFragment!!)
                }

                R.id.settings_navigation -> {
                    showFragment(SettingsFragment())
                }
            }
            return@setOnItemSelectedListener true
        }
        binding.bottomNavigation.selectedItemId = R.id.tasks_navigation
        binding.fabAddTask.setOnClickListener {
            showAddTaskBottomSheet()

        }


    }

    private fun showAddTaskBottomSheet() {
        val addTaskSheet = AddTaskBottomSheet()
        addTaskSheet.tasKAddedListener = AddTaskBottomSheet.OnTasKAddedListener {
            tasksFragment?.getTasksFromDatabase()

        }
        addTaskSheet.show(supportFragmentManager, "")
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
            .commit()


    }


}