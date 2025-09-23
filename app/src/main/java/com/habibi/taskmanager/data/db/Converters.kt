package com.habibi.taskmanager.data.db

import android.annotation.SuppressLint
import androidx.room.TypeConverter
import com.habibi.taskmanager.data.entities.TaskStatus

@SuppressLint("NewApi")
class Converters {

    @TypeConverter
    fun fromTaskStatus(status: TaskStatus): String = status.name

    @TypeConverter
    fun toTaskStatus(status: String): TaskStatus = TaskStatus.valueOf(status)

//    @TypeConverter
//    fun fromTimestamp(value: Long?): LocalDateTime? {
//        return value?.let {
//            Instant.ofEpochMilli(it).atOffset(ZoneOffset.UTC).toLocalDateTime()
//        }
//    }
//
//    @TypeConverter
//    fun dateToTimestamp(date: LocalDateTime?): Long? {
//        return date?.toInstant(ZoneOffset.UTC)?.toEpochMilli()
//    }
}
