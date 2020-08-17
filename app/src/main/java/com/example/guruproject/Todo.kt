package com.example.guruproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class Todo(val month:Int, val date:Int,
                val category: String, val content: String)


//fun addTodo(){
//    //val todo = Todo(binding.editText.text.toString())
//    //data.add(todo)
//    //binding.recyclerView.adapter.notifyDataSetChanged()
//}
//fun deleteTodo(){
//
//}
