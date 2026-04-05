package com.notsad.kisansathi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etLoginPhone = findViewById<EditText>(R.id.etLoginPhone)
        val etLoginPassword = findViewById<EditText>(R.id.etLoginPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val etSignupName = findViewById<EditText>(R.id.etSignupName)
        val etSignupPhone = findViewById<EditText>(R.id.etSignupPhone)
        val etSignupPassword = findViewById<EditText>(R.id.etSignupPassword)
        val btnSignup = findViewById<Button>(R.id.btnSignup)
        val tvStatus = findViewById<TextView>(R.id.tvAuthStatus)

        val prefs = getSharedPreferences("UserAuth", Context.MODE_PRIVATE)

        // Check if already logged in
        if (prefs.getBoolean("isLoggedIn", false)) {
            goToHome()
            return
        }

        btnSignup.setOnClickListener {
            val name = etSignupName.text.toString()
            val phone = etSignupPhone.text.toString()
            val password = etSignupPassword.text.toString()

            if (name.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                tvStatus.text = "⚠️ Please fill all fields"
                tvStatus.setTextColor(android.graphics.Color.RED)
                return@setOnClickListener
            }
            if (phone.length < 10) {
                tvStatus.text = "⚠️ Enter valid phone number"
                tvStatus.setTextColor(android.graphics.Color.RED)
                return@setOnClickListener
            }

            prefs.edit().apply {
                putString("name", name)
                putString("phone", phone)
                putString("password", password)
                apply()
            }
            tvStatus.text = "✅ Account created! Please login."
            tvStatus.setTextColor(android.graphics.Color.parseColor("#2E7D32"))
        }

        btnLogin.setOnClickListener {
            val phone = etLoginPhone.text.toString()
            val password = etLoginPassword.text.toString()

            val savedPhone = prefs.getString("phone", "")
            val savedPassword = prefs.getString("password", "")

            if (phone == savedPhone && password == savedPassword) {
                prefs.edit().putBoolean("isLoggedIn", true).apply()
                goToHome()
            } else {
                tvStatus.text = "⚠️ Invalid phone or password"
                tvStatus.setTextColor(android.graphics.Color.RED)
            }
        }
    }

    private fun goToHome() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}