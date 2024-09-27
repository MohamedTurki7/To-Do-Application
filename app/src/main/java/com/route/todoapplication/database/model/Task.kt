package com.route.todoapplication.database.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    val id: Int = 0,
    @ColumnInfo
    var title: String? = null,
    @ColumnInfo
    var description: String? = null,
    @ColumnInfo(index = true)
    var date: Long? = null,
    @ColumnInfo
    var time: Long? = null,
    @ColumnInfo
    var status: Boolean = false,


    ) : Parcelable
