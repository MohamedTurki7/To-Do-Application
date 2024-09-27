package com.route.todoapplication.ui

import android.app.Application
import com.route.todoapplication.database.AppDatabase

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppDatabase.init(this)
    }

}
