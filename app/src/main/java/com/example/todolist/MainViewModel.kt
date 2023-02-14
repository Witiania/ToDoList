package com.example.todolist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.todolist.data.RoomManager
import com.example.todolist.data.RoomManagerImpl

class MainViewModel(app:Application): AndroidViewModel(app){


    private val roomManager:RoomManager = RoomManagerImpl(app)


    private val todoItemList: MutableLiveData<List<ToDoItem>> = MutableLiveData()
    val todoItemListResult:LiveData<List<ToDoItem>> = todoItemList

    fun getAllData(){
        //TODO Deliver data to view layer
        val result = roomManager.getAllItems()
        todoItemList.postValue(result)
    }

    fun insertItem(item: ToDoItem) {
        roomManager.insertItem(item)
    }

    fun updateItem(item: ToDoItem) {
        roomManager.updateItem(item)
    }

    fun deleteItem(item: ToDoItem) {
        roomManager.deleteItem(item)

    }

}