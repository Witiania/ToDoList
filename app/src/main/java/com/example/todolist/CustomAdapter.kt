package com.example.todolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(private var mList: MutableList<ToDoItem>):RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mList[position]
        holder.title.text = item.title
        holder.description.text = item.description
        holder.number.text = item.number.toString()

    }
    //Добавляем новый элемент в RV
    fun updateList(updatedList:List<ToDoItem>){
        mList = updatedList.toMutableList()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val title: TextView = itemView.findViewById(R.id.item_recycler_title)
        val description: TextView = itemView.findViewById(R.id.item_recycler_description)
        val number: TextView = itemView.findViewById(R.id.item_recycler_number)

    }
}