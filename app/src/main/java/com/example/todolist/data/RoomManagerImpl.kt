package com.example.todolist.data

import android.content.Context
import androidx.room.Room
import com.example.todolist.ToDoItem
import com.example.todolist.room.AppDatabase

/**
 * Use to manage work with room
 */
class RoomManagerImpl(private val context:Context):RoomManager {
    //Инициализируем базу данных
    private var db = Room.databaseBuilder(
        context,
        //Нет контекста: Решается dependency injection, но если его нет, то решается androidViewModel
        //Унаследовали AndroidViewModel в MainViewModel c параметром app
    //вставили context вместо applicationContext,
    AppDatabase::class.java, "database-name"
    ).fallbackToDestructiveMigration()
    .allowMainThreadQueries()
    .build()

//достаем все что есть в БД
    override fun getAllItems():List<ToDoItem> {
        return db.toDoDao().getAllItems()
    }

    override fun insertItem(item: ToDoItem) {
        db.toDoDao().insertItem(item)
    }

    override fun updateItem(item: ToDoItem) {
        db.toDoDao().updateItem(item)
    }

    override fun deleteItem(item: ToDoItem) {
        db.toDoDao().deleteItem(item)
    }
}
