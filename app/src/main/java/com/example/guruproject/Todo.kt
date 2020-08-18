package com.example.guruproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class Todo(val year:Int, val month:Int, val date:Int,
                val category: String, val content: String)

data class Day(val year:Int, val month:Int, val date:Int)