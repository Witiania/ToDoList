package com.example.todolist

import androidx.room.Entity
import androidx.room.PrimaryKey

//Обозначает шаблон столбца в БД (таблице)
@Entity
data class ToDoItem(
    //Обозначает номер строки в таблице, каждый номер уникален
    @PrimaryKey val id: Int,
    val title:String,
    val description:String,
    val number: Int
)
