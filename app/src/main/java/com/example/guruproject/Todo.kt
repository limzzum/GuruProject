package com.example.guruproject



data class Todo(val year:Int, val month:Int, val date:Int,
                val category: String, val content: String)

data class Day(val year:Int, val month:Int, val date:Int)