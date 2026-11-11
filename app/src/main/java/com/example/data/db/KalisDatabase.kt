package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.BookingEntity
import com.example.data.model.EquipmentEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.ReviewEntity
import com.example.data.model.UserEntity

@Database(
    entities = [
        EquipmentEntity::class,
        BookingEntity::class,
        ReviewEntity::class,
        NotificationEntity::class,
        UserEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class KalisDatabase : RoomDatabase() {

    abstract fun kalisDao(): KalisDao

    companion object {
        @Volatile
        private var INSTANCE: KalisDatabase? = null

        fun getDatabase(context: Context): KalisDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KalisDatabase::class.java,
                    "kalis_events_ug.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
