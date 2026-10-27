package com.example.glucosync

import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import kotlin.random.Random
import android.widget.Toast


class ReportsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports)
        Toast.makeText(this, "ReportsActivity opened", Toast.LENGTH_LONG).show()


        val txtWeek = findViewById<TextView>(R.id.txtWeek)
        val txtMonth = findViewById<TextView>(R.id.txtMonth)
        val txtYear = findViewById<TextView>(R.id.txtYear)

        // Fake averages for demo
        val weekAvg = Random.nextInt(90, 140)
        val monthAvg = Random.nextInt(95, 145)
        val yearAvg = Random.nextInt(100, 150)

        txtWeek.text = "Weekly Avg: $weekAvg mg/dL"
        txtMonth.text = "Monthly Avg: $monthAvg mg/dL"
        txtYear.text = "Yearly Avg: $yearAvg mg/dL"
    }
}
