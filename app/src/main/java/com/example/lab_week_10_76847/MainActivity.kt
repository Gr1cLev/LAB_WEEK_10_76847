package com.example.lab_week_10_76847

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.lab_week_10_76847.viewmodels.TotalViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[TotalViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prepareViewModel()
        // Initialize the text with the starting value
        updateText(viewModel.total)
    }

    private fun updateText(total: Int) {
        findViewById<TextView>(R.id.text_total).text =
            getString(R.string.text_total, total)
    }

    private fun prepareViewModel(){
        findViewById<Button>(R.id.button_increment).setOnClickListener {
            viewModel.incrementTotal()
            updateText(viewModel.total)
        }
    }
}
