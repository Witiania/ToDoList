package com.example.todolist.data

import com.example.todolist.ToDoItem

interface RoomManager {
    fun getAllItems():List<ToDoItem>
    fun insertItem(item:ToDoItem)
    fun updateItem(item:ToDoItem)
    fun deleteItem(item: ToDoItem)

}