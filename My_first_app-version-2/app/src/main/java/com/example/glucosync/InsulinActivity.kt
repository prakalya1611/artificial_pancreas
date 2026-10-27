package com.example.glucosync

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import android.view.WindowManager
import android.content.Intent


class InsulinActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔐 BLOCK SCREENSHOTS & SCREEN RECORDING
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        setContentView(R.layout.activity_insulin)

        val edtDose = findViewById<EditText>(R.id.edtDose)
        val btnSend = findViewById<Button>(R.id.btnSendDose)

        btnSend.setOnClickListener {

            val dose = edtDose.text.toString()

            if (dose.isEmpty()) {
                Toast.makeText(this, "Enter insulin units", Toast.LENGTH_SHORT).show()
            } else {

                val intent = Intent(this, MainActivity::class.java)

                intent.putExtra("INSULIN_DATA", "INSULIN:$dose")

                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)

                startActivity(intent)

                Toast.makeText(this, "Tap NFC to send insulin", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
