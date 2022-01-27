package com.example.todoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {
    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                listOfTasks.removeAt(position)
                adapter.notifyDataSetChanged()
                saveItems()
            }
        }
        loadItems()

        // Look up recycler view in the layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        // Create adapter which passes in sample data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)

        // Attach the recycler view to the adapter
        recyclerView.adapter = adapter

        // Set layout manager
        recyclerView.layoutManager = LinearLayoutManager(this)

        val addTaskField = findViewById<EditText>(R.id.addTaskField)
        // Add onclick listener to button
        // Need to check @id/addTaskField text on click
        findViewById<Button>(R.id.button).setOnClickListener {
            val userInputtedTask = addTaskField.text.toString()
            listOfTasks.add(userInputtedTask)
            // Notify Adapter of data change
            adapter.notifyDataSetChanged()
            //adapter.notifyItemInserted(listOfTasks.size - 1)
            addTaskField.setText("")
            saveItems()
        }
    }

    // Save the data the user has changed
    // Saving data by r/w from file

    // Get the relevant data
    fun getDataFile() : File {
        // Every line is going to represent a task in our list of tasks
        return File(filesDir, "data.txt")
    }

    // Load the items by reading every line in data file
    fun loadItems() {
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    // Save the items by writing lines to data file
    fun saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }

    }
}