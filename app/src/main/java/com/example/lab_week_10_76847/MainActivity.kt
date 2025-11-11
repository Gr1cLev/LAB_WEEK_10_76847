package com.example.lab_week_10_76847

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.lab_week_10_76847.database.Total
import com.example.lab_week_10_76847.database.TotalDatabase
import com.example.lab_week_10_76847.database.TotalObject
import com.example.lab_week_10_76847.viewmodels.TotalViewModel
import java.util.Date

class MainActivity : AppCompatActivity() {

    private val db by lazy { prepareDatabase() }

    private val viewModel by lazy {
        ViewModelProvider(this)[TotalViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeValueFromDatabase()
        prepareViewModel()
    }

    override fun onStart() {
        super.onStart()
        // Tampilkan date terakhir via toast
        val list = db.totalDao().getTotal(ID)
        if (list.isNotEmpty()) {
            Toast.makeText(this, list.first().total.date, Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateText(total: Int) {
        findViewById<TextView>(R.id.text_total).text =
            getString(R.string.text_total, total)
    }

    private fun prepareViewModel(){
        viewModel.total.observe(this) { total ->
            updateText(total)
        }
        findViewById<Button>(R.id.button_increment).setOnClickListener {
            viewModel.incrementTotal()
        }
    }

    private fun prepareDatabase(): TotalDatabase {
        return Room.databaseBuilder(
            applicationContext,
            TotalDatabase::class.java,
            "total-database"
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    private fun initializeValueFromDatabase() {
        val list = db.totalDao().getTotal(ID)
        if (list.isEmpty()) {
            db.totalDao().insert(
                Total(
                    id = ID,
                    total = TotalObject(value = 0, date = Date().toString())
                )
            )
            viewModel.setTotal(0)
        } else {
            viewModel.setTotal(list.first().total.value)
        }
    }

    override fun onPause() {
        super.onPause()
        val current = viewModel.total.value ?: 0
        db.totalDao().update(
            Total(
                id = ID,
                total = TotalObject(value = current, date = Date().toString())
            )
        )
    }

    companion object {
        const val ID: Long = 1L
    }
}
