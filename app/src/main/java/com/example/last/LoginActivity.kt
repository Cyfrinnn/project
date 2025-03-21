package com.example.last

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import android.util.Log

class LoginActivity : AppCompatActivity() {

    private var loginType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailEditText: EditText = findViewById(R.id.et_email_login)
        val passwordEditText: EditText = findViewById(R.id.et_password_login)
        val loginButton: Button = findViewById(R.id.btn_login)
        val forgotPasswordTextView: TextView = findViewById(R.id.tv_forgot_password)
        val signUpTextView: TextView = findViewById(R.id.tv_signup)
        val applicantButton: Button = findViewById(R.id.btn_applicant)
        val employerButton: Button = findViewById(R.id.btn_employer)

        // Set login type as "applicant"
        applicantButton.setOnClickListener {
            loginType = "applicant"
            Toast.makeText(this, "Login Type: $loginType", Toast.LENGTH_SHORT).show()
        }

        // Set login type as "employer"
        employerButton.setOnClickListener {
            loginType = "employer"
            Toast.makeText(this, "Login Type: $loginType", Toast.LENGTH_SHORT).show()
        }

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Check if the login type is selected
            if (loginType == null) {
                Toast.makeText(this, "Please select Applicant or Employer", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Log the request parameters
            Log.d("LoginDebug", "Request Params: email=$email, loginType=$loginType")

            // Perform the actual network request for login
            val params = mapOf(
                "email" to email,
                "password" to password,
                "loginType" to (loginType ?: "")
            )

            // Log the request parameters map
            Log.d("LoginDebug", "Request Params Map: $params")

            NetworkUtils.post("http://10.0.2.2/api/login.php", params, object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread {
                        Toast.makeText(this@LoginActivity, "Login failed: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    val responseBody = response.body?.string()

                    // Log the raw response
                    Log.d("LoginDebug", "Response Body: $responseBody")

                    // Parse the response
                    val responseType: Type = object : TypeToken<Map<String, String>>() {}.type
                    val responseMap: Map<String, String> = Gson().fromJson(responseBody, responseType)

                    runOnUiThread {
                        if (response.isSuccessful && responseMap["message"] != null) {
                            Toast.makeText(this@LoginActivity, responseMap["message"].toString(), Toast.LENGTH_SHORT).show()

                            // Check the login type and navigate accordingly
                            if (loginType == "employer") {
                                Log.d("LoginDebug", "Navigating to EmployerMainActivity")
                                val intent = Intent(this@LoginActivity, EmployerMainActivity::class.java)
                                startActivity(intent)
                            } else if (loginType == "applicant") {
                                Log.d("LoginDebug", "Navigating to ApplicantMainActivity")
                                val intent = Intent(this@LoginActivity, ApplicantMainActivity::class.java)
                                startActivity(intent)
                            }
                            finish() // Prevent going back to the LoginActivity
                        } else if (responseMap["error"] != null) {
                            Toast.makeText(this@LoginActivity, responseMap["error"].toString(), Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@LoginActivity, "Unexpected error occurred", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        }

        // Handle forgot password logic
        forgotPasswordTextView.setOnClickListener {
            Toast.makeText(this, "Forgot password clicked", Toast.LENGTH_SHORT).show()
        }

        // Navigate to sign-up page
        signUpTextView.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}
