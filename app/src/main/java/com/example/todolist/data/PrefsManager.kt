package com.example.todolist.data

import com.example.todolist.ToDoItem

interface PrefsManager {
    fun getToDoItem():ToDoItem
    fun saveDataInPrefs(key:String, value: String)

}