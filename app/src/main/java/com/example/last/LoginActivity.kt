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

class LoginActivity : AppCompatActivity() {

    private var loginType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // **Session Validation Logic**
        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        val userId = sharedPreferences.getString("user_id", null)
        val loginTypeFromSession = sharedPreferences.getString("login_type", null)

        // Debug logging to verify retrieved session data
        Log.d("SessionDebug", "Retrieved User ID: $userId, Login Type: $loginTypeFromSession")

        if (userId != null && loginTypeFromSession != null) {
            // Session exists, navigate based on login type
            when (loginTypeFromSession) {
                "employer" -> {
                    startActivity(Intent(this, EmployerMainActivity::class.java))
                }
                "applicant" -> {
                    startActivity(Intent(this, ApplicantMainActivity::class.java))
                }
            }
            finish() // Prevent returning to login screen
        }

        setContentView(R.layout.activity_login) // Show login screen if no session exists

        val emailEditText: EditText = findViewById(R.id.et_email_login)
        val passwordEditText: EditText = findViewById(R.id.et_password_login)
        val loginButton: Button = findViewById(R.id.btn_login)
        val forgotPasswordTextView: TextView = findViewById(R.id.tv_forgot_password)
        val signUpTextView: TextView = findViewById(R.id.tv_signup)
        val applicantButton: Button = findViewById(R.id.btn_applicant)
        val employerButton: Button = findViewById(R.id.btn_employer)

        // Select login type
        applicantButton.setOnClickListener {
            loginType = "applicant"
            Toast.makeText(this, "Login Type: Applicant", Toast.LENGTH_SHORT).show()
        }

        employerButton.setOnClickListener {
            loginType = "employer"
            Toast.makeText(this, "Login Type: Employer", Toast.LENGTH_SHORT).show()
        }

        // Handle login button click
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (loginType == null) {
                Toast.makeText(this, "Please select Applicant or Employer", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email and Password are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Prepare login parameters
            val params = mapOf("email" to email, "password" to password, "loginType" to loginType!!)
            Log.d("LoginDebug", "Request Params: $params")

            // Send login request
            NetworkUtils.post("http://192.168.1.10/api/login.php", params, object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread {
                        Toast.makeText(this@LoginActivity, "Network error: ${e.message}", Toast.LENGTH_SHORT).show()
                        Log.e("LoginDebug", "Network error: ${e.message}")
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    val responseBody = response.body?.string()
                    Log.d("LoginDebug", "Response Body: $responseBody")

                    // Parse JSON response
                    val responseType: Type = object : TypeToken<Map<String, String>>() {}.type
                    val responseMap: Map<String, String> = Gson().fromJson(responseBody, responseType)

                    runOnUiThread {
                        if (response.isSuccessful && responseMap["status"] == "success") {
                            Toast.makeText(this@LoginActivity, responseMap["message"] ?: "Login successful", Toast.LENGTH_SHORT).show()

                            // Debug logging for server response before saving
                            Log.d("SessionDebug", "Server Response: User ID=${responseMap["user_id"]}, Login Type=${responseMap["role"]}")

                            // Save session data
                            val editor = sharedPreferences.edit()
                            editor.putString("user_id", responseMap["user_id"])
                            editor.putString("login_type", responseMap["role"]) // Save login type
                            editor.apply()

                            Log.d("SessionDebug", "Saved User ID: ${responseMap["user_id"]}, Login Type: ${responseMap["role"]}")

                            // Navigate based on login type
                            when (loginType) {
                                "employer" -> startActivity(Intent(this@LoginActivity, EmployerMainActivity::class.java))
                                "applicant" -> startActivity(Intent(this@LoginActivity, ApplicantMainActivity::class.java))
                            }
                            finish() // Prevent returning to login
                        } else {
                            Toast.makeText(this@LoginActivity, responseMap["error"] ?: "Login failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        }

        // Forgot password functionality (can be expanded later)
        forgotPasswordTextView.setOnClickListener {
            Toast.makeText(this, "Forgot Password clicked", Toast.LENGTH_SHORT).show()
        }

        // Navigate to sign-up activity
        signUpTextView.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}
