package com.example.lab_week_10_76847.viewmodels

import androidx.lifecycle.ViewModel

class TotalViewModel: ViewModel() {
    var total: Int = 0
        private set

    fun incrementTotal() {
        total++
    }
}
