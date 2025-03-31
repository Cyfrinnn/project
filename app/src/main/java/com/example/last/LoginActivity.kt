package com.example.last

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.lang.reflect.Type
import android.content.SharedPreferences


class LoginActivity : AppCompatActivity() {

    private var loginType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // **Session Validation Logic**
        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear() // Clear any existing session
        editor.apply()
        val userId = sharedPreferences.getString("user_id", null)
        val loginTypeFromSession = sharedPreferences.getString("login_type", null)

        // Debug logging to verify retrieved session data
        Log.d("SessionDebug", "User ID: $userId, Login Type: $loginTypeFromSession")

        if (!userId.isNullOrEmpty() && !loginTypeFromSession.isNullOrEmpty()) {
            Log.d("SessionDebug", "Valid session detected. Redirecting user...")
            navigateBasedOnLoginType(loginTypeFromSession)
            finish() // Prevent returning to login screen
        } else {
            Log.d("SessionDebug", "No valid session. Staying on login screen.")
        }


        setContentView(R.layout.activity_login) // Show login screen if no session exists

        // Initialize UI elements
        val emailEditText: EditText = findViewById(R.id.et_email_login)
        val passwordEditText: EditText = findViewById(R.id.et_password_login)
        val loginButton: Button = findViewById(R.id.btn_login)
        val applicantButton: Button = findViewById(R.id.btn_applicant)
        val employerButton: Button = findViewById(R.id.btn_employer)
        val forgotPasswordTextView: TextView = findViewById(R.id.tv_forgot_password)
        val signUpTextView: TextView = findViewById(R.id.tv_signup)

        // Set up login type buttons
        setupButtonListeners(applicantButton, employerButton)

        // Handle login button click
        loginButton.setOnClickListener {
            handleLogin(emailEditText, passwordEditText, sharedPreferences)
        }

        // Forgot password functionality
        forgotPasswordTextView.setOnClickListener {
            startActivity(Intent(this, ForgotPassPhoneNumber::class.java))
        }

        // Navigate to sign-up activity
        signUpTextView.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun setupButtonListeners(applicantButton: Button, employerButton: Button) {
        applicantButton.setOnClickListener {
            loginType = "applicant"
            applicantButton.setBackgroundResource(R.drawable.for_btn_hovered)
            employerButton.setBackgroundResource(R.drawable.for_btn)
            Toast.makeText(this, "Login Type: Applicant", Toast.LENGTH_SHORT).show()
        }

        employerButton.setOnClickListener {
            loginType = "employer"
            employerButton.setBackgroundResource(R.drawable.for_btn_hovered)
            applicantButton.setBackgroundResource(R.drawable.for_btn)
            Toast.makeText(this, "Login Type: Employer", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleLogin(emailEditText: EditText, passwordEditText: EditText, sharedPreferences: SharedPreferences) {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (loginType == null) {
            Toast.makeText(this, "Please select Applicant or Employer", Toast.LENGTH_SHORT).show()
            return
        }

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email and Password are required", Toast.LENGTH_SHORT).show()
            return
        }

        val params = mapOf("email" to email, "password" to password, "loginType" to loginType!!)
        Log.d("LoginDebug", "Request Params: $params")

        NetworkUtils.post("http://10.0.2.2/api/login.php", params, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@LoginActivity, "Network error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d("LoginDebug", "Response Body: $responseBody")

                val responseType: Type = object : TypeToken<Map<String, String>>() {}.type
                val responseMap: Map<String, String> = Gson().fromJson(responseBody, responseType)

                runOnUiThread {
                    if (response.isSuccessful && responseMap["status"] == "success") {
                        handleSuccessfulLogin(responseMap, sharedPreferences)
                    } else {
                        Toast.makeText(this@LoginActivity, responseMap["error"] ?: "Login failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun handleSuccessfulLogin(responseMap: Map<String, String>, sharedPreferences: SharedPreferences) {
        Toast.makeText(this, responseMap["message"] ?: "Login successful", Toast.LENGTH_SHORT).show()

        val editor = sharedPreferences.edit()
        editor.putString("user_id", responseMap["user_id"])
        editor.putString("employer_id", responseMap["employer_id"])
        editor.putString("login_type", loginType!!)
        editor.apply()

        navigateBasedOnLoginType(loginType!!)
        finish()
    }

    private fun navigateBasedOnLoginType(loginType: String) {
        when (loginType) {
            "employer" -> startActivity(Intent(this, EmployerMainActivity::class.java))
            "applicant" -> startActivity(Intent(this, ApplicantMainActivity::class.java))
        }
    }
}
