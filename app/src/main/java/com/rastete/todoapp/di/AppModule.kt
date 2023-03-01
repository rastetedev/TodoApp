package com.rastete.todoapp.di

import android.content.Context
import com.rastete.todoapp.data.db.TodoDatabase
import com.rastete.todoapp.data.repository.TodoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TodoDatabase {
        return TodoDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideRepository(todoDatabase: TodoDatabase): TodoRepository {
        return TodoRepository(todoDao = todoDatabase.todoDao())
    }

}