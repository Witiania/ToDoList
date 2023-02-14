package com.example.todolist.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.todolist.ToDoItem

@Dao
interface ToDoDao {

    //Вытащить что-то(так как не указано что, вытаскиваем все)из таблицы (БД) и показать
    @Query("SELECT * FROM todoitem")
    fun getAllItems(): List<ToDoItem>

    //Вдоизменяемые аннотации таблицы:
    //Вставляем item
    @Insert
    fun insertItem(toDoItem: ToDoItem)

    //Удаляем Item
    @Delete
    fun deleteItem(toDoItem: ToDoItem)

    //Обновляем данные
    @Update
    fun updateItem(toDoItem: ToDoItem)

}
