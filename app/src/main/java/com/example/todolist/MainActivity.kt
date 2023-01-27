package com.example.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.LinearLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.todolist.room.AppDatabase
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(),OnItemClick {

    private lateinit var stubContainer: LinearLayout
    private lateinit var fab: FloatingActionButton
    private lateinit var recyclerview: RecyclerView
    private lateinit var adapter: CustomAdapter
    private lateinit var db:AppDatabase

    //№3 Для обработки данных создаем LiveData
    private lateinit var todoLiveData: LiveData<List<ToDoItem>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // getting the recyclerview by its id
        recyclerview = findViewById<RecyclerView>(R.id.main_recycler_view)
        stubContainer = findViewById(R.id.main_no_items_container)
        fab = findViewById(R.id.main_fub)

        fab.setOnClickListener {
            //№1 Появление диалогового окна для сбора информации - переход на CustomDialog
            val dialog = CustomDialog(this)
            dialog.show()
        }

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)
        adapter = CustomAdapter(mutableListOf(),this)
        recyclerview.adapter = adapter

        //Инициализируем базу данных
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
        //№3.1 Инициализировали LiveData, достаем все что есть в БД
        todoLiveData = db.toDoDao().getAllItems()

        //#4 Отображаем полученные данные в списке
        //Осервер слушает (принимает данные из LiveData) и отправляет в RV где происходит обновление списка
        todoLiveData.observe(this, Observer {
            adapter.updateList(it)

            if (it.isEmpty()) {
                stubContainer.visibility = VISIBLE
                recyclerview.visibility = INVISIBLE
                Log.d("testLog", "List empty")
            } else {
                stubContainer.visibility = INVISIBLE
                recyclerview.visibility = VISIBLE
                Log.d("testLog", "List NOT empty")
            }

            Log.d("roomCheckLog","->$it")
        })
    }
    //№2.2 с помощью метода InsertItem Добавляем Item с данными в БД
    fun addItem(item:ToDoItem){
        stubContainer.visibility = INVISIBLE
        recyclerview.visibility = VISIBLE
        db.toDoDao().insertItem(item)
           }

    override fun itemClicked(item: ToDoItem) {
      Log.d("OnClickedItem","itemClicked $item")
    }
}