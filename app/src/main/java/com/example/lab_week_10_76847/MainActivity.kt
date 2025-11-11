package com.example.lab_week_10_76847

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.lab_week_10_76847.database.Total
import com.example.lab_week_10_76847.database.TotalDatabase
import com.example.lab_week_10_76847.viewmodels.TotalViewModel

class MainActivity : AppCompatActivity() {

    // Create an instance of the TotalDatabase
    // by lazy is used to create the database only when it's needed
    private val db by lazy { prepareDatabase() }

    // Create an instance of the TotalViewModel
    // by lazy is used to create the ViewModel only when it's needed
    private val viewModel by lazy {
        ViewModelProvider(this)[TotalViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the value of the total from the database
        initializeValueFromDatabase()

        // Prepare the ViewModel
        prepareViewModel()
    }

    private fun updateText(total: Int) {
        findViewById<TextView>(R.id.text_total).text =
            getString(R.string.text_total, total)
    }

    private fun prepareViewModel(){
        // Observe the LiveData object
        viewModel.total.observe(this) { total ->
            // Whenever the value of the LiveData object changes
            // the updateText() is called, with the new value as the parameter
            updateText(total)
        }

        findViewById<Button>(R.id.button_increment).setOnClickListener {
            viewModel.incrementTotal()
        }
    }

    // Create and build the TotalDatabase with the name 'total-database'
    // allowMainThreadQueries() is used to allow queries to be run on the main thread
    // This is not recommended, but for simplicity it's used here
    private fun prepareDatabase(): TotalDatabase {
        return Room.databaseBuilder(
            applicationContext,
            TotalDatabase::class.java,
            "total-database"
        ).allowMainThreadQueries().build()
    }

    // Initialize the value of the total from the database
    // If the database is empty, insert a new Total object with the value of 0
    // If the database is not empty, get the value of the total from the database
    private fun initializeValueFromDatabase() {
        val list = db.totalDao().getTotal(ID)
        if (list.isEmpty()) {
            db.totalDao().insert(Total(id = ID, total = 0))
            setTotal(0)
        } else {
            setTotal(list.first().total)
        }
    }

    // helper untuk set total ke ViewModel (karena modul memanggil setTotal)
    private fun setTotal(value: Int) {
        viewModel.setTotal(value)
    }

    // Update the value of the total in the database whenever the activity is paused
    // This is done to ensure that the value of the total is always up to date even if the app is closed
    override fun onPause() {
        super.onPause()
        val current = viewModel.total.value ?: 0
        db.totalDao().update(Total(ID, current))
    }

    companion object {
        // The ID of the Total object in the database
        // For simplicity, we only have one Total object in the database
        // So the ID is always 1
        const val ID: Long = 1L
    }
}
