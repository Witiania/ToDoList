package com.example.todolist

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.todolist.room.AppDatabase
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(),OnItemClick {

    //Даем доступ к ViewModel
    private val mMainViewModel:MainViewModel by viewModels()

    private lateinit var stubContainer: LinearLayout
    private lateinit var fab: FloatingActionButton
    private lateinit var recyclerview: RecyclerView
    private lateinit var adapter: CustomAdapter
    //



    private lateinit var data:List<ToDoItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // getting the recyclerview by its id
        recyclerview = findViewById<RecyclerView>(R.id.main_recycler_view)
        stubContainer = findViewById(R.id.main_no_items_container)
        fab = findViewById(R.id.main_fub)

        fab.setOnClickListener {
            //№1 Появление диалогового окна для сбора информации - переход на CustomDialog

            val dialog = CustomDialog(this, true, null)
            dialog.show()
        }

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)
        adapter = CustomAdapter(mutableListOf(),this)
        recyclerview.adapter = adapter



        mMainViewModel.getAllData()
        //#4 Отображаем полученные данные в списке
        //Осервер слушает (принимает данные из LiveData) и отправляет в RV где происходит обновление списка
        mMainViewModel.todoItemListResult.observe(this, Observer {
            data = it
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

        val deleteIcon = ContextCompat.getDrawable(this, R.drawable.ic_delete_24)
        val intrinsicWidth = deleteIcon?.intrinsicWidth
        val intrinsicHeight = deleteIcon?.intrinsicHeight
        val background = ColorDrawable()
        val backgroundColor = Color.parseColor("#f44336")
        val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                // this method is called
                // when the item is moved.
                return false
            }

            // Let's draw our delete view
            override fun onChildDraw(canvas: Canvas, recyclerView:RecyclerView, viewHolder:RecyclerView.ViewHolder,dx:Float,dy:Float,actionState:Int,isCurrentlyActive:Boolean) {
                val itemView = viewHolder.itemView
                val itemHeight = itemView.bottom - itemView.top

                // Draw the red delete background
                background.color = backgroundColor
                background.setBounds(
                    itemView.right + dx.toInt(),
                    itemView.top,
                    itemView.right,
                    itemView.bottom
                )
                background.draw(canvas)

                // Calculate position of delete icon
                val iconTop = itemView.top + (itemHeight - intrinsicHeight!!) / 2
                val iconMargin = (itemHeight - intrinsicHeight!!) / 2
                val iconLeft = itemView.right - iconMargin - intrinsicWidth!!
                val iconRight = itemView.right - iconMargin
                val iconBottom = iconTop + intrinsicHeight!!

                // Draw the delete icon
                deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                deleteIcon.draw(canvas)
            }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // this method is called when we swipe our item to right direction.
                // on below line we are getting the item at a particular position.
                val deletedToDoItem: ToDoItem =
                    data.get(viewHolder.adapterPosition)



                // below line is to get the position
                // of the item at that position.
                val position = viewHolder.adapterPosition

                // this method is called when item is swiped.
                // below line is to remove item from our array list.
                data.toMutableList().removeAt(position)

                // below line is to notify our item is removed from adapter.
                adapter.notifyItemRemoved(position)

                // below line is to display our snackbar with action.
                // below line is to display our snackbar with action.
                // below line is to display our snackbar with action.
                Snackbar.make(recyclerview, "Deleted " + deletedToDoItem.title, Snackbar.LENGTH_LONG)
                    .setAction(
                        "Undo",
                        View.OnClickListener {
                            // adding on click listener to our action of snack bar.
                            // below line is to add our item to array list with a position.
                            data.toMutableList().add(position, deletedToDoItem)

                            // below line is to notify item is
                            // added to our adapter class.
                            adapter.notifyItemInserted(position)
                        }).show()
                 deleteItem(deletedToDoItem)
            }
            // at last we are adding this
            // to our recycler view.
        }).attachToRecyclerView(recyclerview)

    }


    //№2.2 с помощью метода InsertItem Добавляем Item с данными в БД
    fun addItem(item:ToDoItem){
        stubContainer.visibility = INVISIBLE
        recyclerview.visibility = VISIBLE
        mMainViewModel.insertItem(item)
           }

    fun updateItem(item: ToDoItem){
        mMainViewModel.updateItem(item)
    }
    fun deleteItem(item: ToDoItem){
       mMainViewModel.deleteItem(item)
    }

    override fun itemClicked(item: ToDoItem) {
        val dialog = CustomDialog(this,false,item)
        dialog.show()
    }
}
