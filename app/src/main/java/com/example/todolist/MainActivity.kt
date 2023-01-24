package com.example.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Adapter
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var stubContainer: LinearLayout
    private lateinit var fub: FloatingActionButton
    private lateinit var recyclerview: RecyclerView
    private lateinit var adapter: CustomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // getting the recyclerview by its id
        recyclerview = findViewById<RecyclerView>(R.id.main_recycler_view)
        stubContainer = findViewById(R.id.main_no_items_container)
        fub = findViewById(R.id.main_fub)

        fub.setOnClickListener {
            adapter.addItem(ToDoItem("New_Title","It WOKS!",444))
            Log.d("testLog","add item")
        }

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        //Создаем проверку нашего Адаптера, используя цикл
        // ArrayList of class ItemsViewModel
        val data = ArrayList<ToDoItem>()

        // This loop will create 100 Views containing
        for (item in 1..100) {
            data.add(ToDoItem("title", "description",item))
        }

        //Проверка, пустой ли список RV
        if(data.isEmpty()){
            stubContainer.visibility = VISIBLE
            recyclerview.visibility = INVISIBLE
            Log.d("testLog","List empty")
        }else{
            stubContainer.visibility = INVISIBLE
            recyclerview.visibility = VISIBLE
            Log.d("testLog","List NOT empty")
        }


        // This will pass the ArrayList to our Adapter
            adapter = CustomAdapter(data)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter

    }

}