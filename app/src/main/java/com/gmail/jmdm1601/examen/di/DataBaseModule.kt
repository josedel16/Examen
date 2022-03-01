package com.gmail.jmdm1601.examen.di

import android.content.Context
import com.gmail.jmdm1601.examen.data.local.AppDataBase
import com.gmail.jmdm1601.examen.data.local.MoviesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataBaseModule {

    @Singleton
    @Provides
    fun provideAppDataBase(@ApplicationContext context: Context): AppDataBase {
        return AppDataBase.getInstance(context)
    }

    @Provides
    fun provideMoviesDao(appDataBase: AppDataBase): MoviesDao {
        return appDataBase.moviesDao()
    }
}