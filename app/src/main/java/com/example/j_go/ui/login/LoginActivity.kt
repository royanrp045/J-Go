package com.example.j_go.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.j_go.MainActivity
import com.example.j_go.R
import com.example.j_go.ui.register.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        val emailInput: EditText = findViewById(R.id.emailInput)
        val passwordInput: EditText = findViewById(R.id.passwordInput)
        val emailError: TextView = findViewById(R.id.emailError)
        val passwordError: TextView = findViewById(R.id.passwordError)
        val signInButton: Button = findViewById(R.id.signInButton)
        val signUpLink: TextView = findViewById(R.id.signUpLink)

        signInButton.setOnClickListener {
            // Reset error visibility
            emailError.visibility = View.GONE
            passwordError.visibility = View.GONE

            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            var isValid = true

            // Email validation
            if (email.isEmpty()) {
                emailError.text = getString(R.string.email_required)
                emailError.visibility = View.VISIBLE
                isValid = false
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailError.text = getString(R.string.invalid_email)
                emailError.visibility = View.VISIBLE
                isValid = false
            }

            // Password validation
            if (password.isEmpty()) {
                passwordError.text = getString(R.string.password_required)
                passwordError.visibility = View.VISIBLE
                isValid = false
            } else if (password.length < 6) {
                passwordError.text = getString(R.string.password_too_short)
                passwordError.visibility = View.VISIBLE
                isValid = false
            }

            if (!isValid) {
                return@setOnClickListener
            }

            // Login Firebase
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid
                        val userRef = FirebaseDatabase.getInstance().getReference("users").child(userId!!)

                        userRef.get().addOnSuccessListener { snapshot ->
                            val name = snapshot.child("name").getValue(String::class.java) ?: "User Name"

                            // Save to SharedPreferences
                            val sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)
                            sharedPreferences.edit().apply {
                                putString("user_id", userId)
                                putString("email", email)
                                putString("name", name)
                                apply()
                            }

                            Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show()

                            // Navigate to MainActivity
                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()

                        }.addOnFailureListener {
                            Toast.makeText(this, getString(R.string.error_fetch_user_data), Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        Toast.makeText(this, "${getString(R.string.login_failed)}: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        // Handle Sign Up link
        signUpLink.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
