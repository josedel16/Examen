package com.gmail.jmdm1601.examen.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gmail.jmdm1601.examen.core.Constants
import com.gmail.jmdm1601.examen.data.model.MovieEntity

@Database(entities = [MovieEntity::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun moviesDao(): MoviesDao

    companion object {
        @Volatile
        private var instance: AppDataBase? = null

        fun getInstance(context: Context): AppDataBase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDataBase {
            return Room.databaseBuilder(context, AppDataBase::class.java, Constants.DATABASE_NAME)
                .build()
        }
    }
}