package com.example.last

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class SignUpActivity : AppCompatActivity() {

    private var isApplicantSignUp = false
    private val PICK_FILE_REQUEST_CODE = 1
    private var selectedFileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val applicantButton: Button = findViewById(R.id.btn_applicant)
        val employerButton: Button = findViewById(R.id.btn_employer)
        val signUpButton: Button = findViewById(R.id.btn_signup)
        val attachFileButton: Button = findViewById(R.id.btn_attach_file)
        val loginTextView: TextView = findViewById(R.id.tv_login)

        val fullNameEditText: EditText = findViewById(R.id.et_full_name)
        val companyNameEditText: EditText = findViewById(R.id.et_company_name)

        val applicantEmailEditText: EditText = findViewById(R.id.et_applicant_email)
        val applicantPasswordEditText: EditText = findViewById(R.id.et_applicant_password)
        val applicantConfirmPasswordEditText: EditText = findViewById(R.id.et_applicant_confirm_password)

        val employerEmailEditText: EditText = findViewById(R.id.et_employer_email)
        val employerPasswordEditText: EditText = findViewById(R.id.et_employer_password)
        val employerConfirmPasswordEditText: EditText = findViewById(R.id.et_employer_confirm_password)

        applicantButton.setOnClickListener {
            isApplicantSignUp = true
            toggleSignUpFields(
                fullNameEditText, companyNameEditText,
                applicantEmailEditText, applicantPasswordEditText, applicantConfirmPasswordEditText,
                employerEmailEditText, employerPasswordEditText, employerConfirmPasswordEditText,
                signUpButton, attachFileButton
            )
            Toast.makeText(this, "Applicant selected", Toast.LENGTH_SHORT).show()
        }

        employerButton.setOnClickListener {
            isApplicantSignUp = false
            toggleSignUpFields(
                fullNameEditText, companyNameEditText,
                applicantEmailEditText, applicantPasswordEditText, applicantConfirmPasswordEditText,
                employerEmailEditText, employerPasswordEditText, employerConfirmPasswordEditText,
                signUpButton, attachFileButton
            )
            Toast.makeText(this, "Employer selected", Toast.LENGTH_SHORT).show()
        }

        signUpButton.setOnClickListener {
            val nameOrCompany = if (isApplicantSignUp) fullNameEditText.text.toString().trim() else companyNameEditText.text.toString().trim()
            val email = if (isApplicantSignUp) applicantEmailEditText.text.toString().trim() else employerEmailEditText.text.toString().trim()
            val password = if (isApplicantSignUp) applicantPasswordEditText.text.toString().trim() else employerPasswordEditText.text.toString().trim()
            val confirmPassword = if (isApplicantSignUp) applicantConfirmPasswordEditText.text.toString().trim() else employerConfirmPasswordEditText.text.toString().trim()

            if (nameOrCompany.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Invalid email address!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (isApplicantSignUp) {
                signUpApplicant(nameOrCompany, email, password)
            } else {
                signUpEmployer(nameOrCompany, email, password)
            }
        }

        attachFileButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            startActivityForResult(intent, PICK_FILE_REQUEST_CODE)
        }

        loginTextView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun toggleSignUpFields(
        fullNameEditText: EditText, companyNameEditText: EditText,
        applicantEmailEditText: EditText, applicantPasswordEditText: EditText, applicantConfirmPasswordEditText: EditText,
        employerEmailEditText: EditText, employerPasswordEditText: EditText, employerConfirmPasswordEditText: EditText,
        signUpButton: Button, attachFileButton: Button
    ) {
        if (isApplicantSignUp) {
            fullNameEditText.visibility = View.VISIBLE
            companyNameEditText.visibility = View.GONE
            applicantEmailEditText.visibility = View.VISIBLE
            applicantPasswordEditText.visibility = View.VISIBLE
            applicantConfirmPasswordEditText.visibility = View.VISIBLE

            employerEmailEditText.visibility = View.GONE
            employerPasswordEditText.visibility = View.GONE
            employerConfirmPasswordEditText.visibility = View.GONE
            attachFileButton.visibility = View.GONE

            signUpButton.layoutParams = (signUpButton.layoutParams as RelativeLayout.LayoutParams).apply {
                addRule(RelativeLayout.BELOW, R.id.et_applicant_confirm_password)
            }

        } else {
            fullNameEditText.visibility = View.GONE
            companyNameEditText.visibility = View.VISIBLE
            applicantEmailEditText.visibility = View.GONE
            applicantPasswordEditText.visibility = View.GONE
            applicantConfirmPasswordEditText.visibility = View.GONE

            employerEmailEditText.visibility = View.VISIBLE
            employerPasswordEditText.visibility = View.VISIBLE
            employerConfirmPasswordEditText.visibility = View.VISIBLE
            attachFileButton.visibility = View.VISIBLE

            signUpButton.layoutParams = (signUpButton.layoutParams as RelativeLayout.LayoutParams).apply {
                addRule(RelativeLayout.BELOW, R.id.btn_attach_file)
            }
        }
    }

    private fun signUpApplicant(fullName: String, email: String, password: String) {
        val client = OkHttpClient()
        val json = JSONObject().apply {
            put("full_name", fullName)
            put("email", email)
            put("password", password)
        }

        val body = json.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
        val request = Request.Builder()
            .url("http://192.168.1.10/api/add_applicant.php")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@SignUpActivity, "Network error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val jsonResponse = JSONObject(responseBody ?: "")

                    // Check if the server returned an error
                    if (jsonResponse.has("error")) {
                        runOnUiThread {
                            Toast.makeText(this@SignUpActivity, jsonResponse.getString("error"), Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this@SignUpActivity, "Sign up successful!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@SignUpActivity, ConfirmationActivity::class.java))
                            finish()
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@SignUpActivity, "Sign up failed: ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
    private fun signUpEmployer(companyName: String, email: String, password: String) {
        val client = OkHttpClient()
        val json = JSONObject().apply {
            put("company_name", companyName)
            put("email", email)
            put("password", password)
        }

        val body = json.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
        val request = Request.Builder()
            .url("http://192.168.1.10/api/add_employer.php")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@SignUpActivity, "Network error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val jsonResponse = JSONObject(responseBody ?: "")

                    // Check if the server returned an error
                    if (jsonResponse.has("error")) {
                        runOnUiThread {
                            Toast.makeText(this@SignUpActivity, jsonResponse.getString("error"), Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this@SignUpActivity, "Sign up successful!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@SignUpActivity, ConfirmationActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@SignUpActivity, "Sign up failed: ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            data?.data?.let { fileUri ->
                selectedFileUri = fileUri
                val fileName = fileUri.path?.let { File(it).name }
                findViewById<TextView>(R.id.tv_file_name).text = fileName
            }
        }
    }
}
