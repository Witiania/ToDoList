package com.example.todolist.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todolist.ToDoItem

//Указываем какие типы данныз будут храниться
//При наполнении базы данных, версию нужно увеличивать иначе будет ошибка, либо удалить и поставить заново приложение в самом эмуляторе
@Database(entities = [ToDoItem::class], version = 1,exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun toDoDao(): ToDoDao
}