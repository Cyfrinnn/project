package com.example.last

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val applicantButton: Button = findViewById(R.id.btn_applicant)
        val employerButton: Button = findViewById(R.id.btn_employer)
        val signUpButton: Button = findViewById(R.id.btn_signup)
        val loginTextView: TextView = findViewById(R.id.tv_login)

        applicantButton.setOnClickListener {
            // Handle applicant sign-up option
            Toast.makeText(this, "Applicant selected", Toast.LENGTH_SHORT).show()
        }

        employerButton.setOnClickListener {
            // Handle employer sign-up option
            Toast.makeText(this, "Employer selected", Toast.LENGTH_SHORT).show()
        }

        signUpButton.setOnClickListener {
            val fullName: EditText = findViewById(R.id.et_full_name)
            val email: EditText = findViewById(R.id.et_email)
            val password: EditText = findViewById(R.id.et_password)
            val confirmPassword: EditText = findViewById(R.id.et_confirm_password)

            // Handle sign-up logic
            if (password.text.toString() == confirmPassword.text.toString()) {
                Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }
        }

        loginTextView.setOnClickListener {
            // Handle login link click
            Toast.makeText(this, "Redirect to login", Toast.LENGTH_SHORT).show()
        }
    }
}
