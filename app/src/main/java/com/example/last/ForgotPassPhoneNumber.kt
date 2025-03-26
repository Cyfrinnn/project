package com.example.last

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ForgotPassPhoneNumber : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass_phone_number)

        // Handle system bar insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.forgot_pass_ph_number)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val phoneNumberField = findViewById<EditText>(R.id.et_phone_number)
        val proceedButton = findViewById<Button>(R.id.btn_proceed)
        val backButton = findViewById<ImageButton>(R.id.back_button)

        // TextWatcher to monitor phone number input
        phoneNumberField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {}
        })

        // Back button functionality
        backButton.setOnClickListener {
            finish() // Go back to the previous activity
        }

        // Proceed button functionality
        proceedButton.setOnClickListener {
            val phoneNumber = phoneNumberField.text.toString().trim()

            if (phoneNumber.isEmpty()) {
                Toast.makeText(this, "Phone number must not be empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (phoneNumber.length != 11) {
                Toast.makeText(this, "Phone number must be exactly 11 digits!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // If valid, proceed to ForgotPassOTP
            val intent = Intent(this, ForgotPassOTP::class.java)
            startActivity(intent)
        }
    }
}