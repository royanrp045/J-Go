package com.example.j_go.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.j_go.R
import com.example.j_go.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        val nameInput: EditText = findViewById(R.id.nameInput)
        val emailInput: EditText = findViewById(R.id.emailInput)
        val passwordInput: EditText = findViewById(R.id.passwordInput)
        val nameError: TextView = findViewById(R.id.nameError)
        val emailError: TextView = findViewById(R.id.emailError)
        val passwordError: TextView = findViewById(R.id.passwordError)
        val signUpButton: Button = findViewById(R.id.signUpButton)
        val signInLink: TextView = findViewById(R.id.signInLink)

        // Handle Sign Up button click
        signUpButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            // Reset errors
            nameError.visibility = View.GONE
            emailError.visibility = View.GONE
            passwordError.visibility = View.GONE

            // Validate inputs
            var isValid = true
            if (name.isEmpty()) {
                nameError.setText(R.string.name_required)
                nameError.visibility = View.VISIBLE
                isValid = false
            }

            if (email.isEmpty()) {
                emailError.setText(R.string.email_required)
                emailError.visibility = View.VISIBLE
                isValid = false
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailError.setText(R.string.invalid_email)
                emailError.visibility = View.VISIBLE
                isValid = false
            }

            if (password.isEmpty()) {
                passwordError.setText(R.string.password_required)
                passwordError.visibility = View.VISIBLE
                isValid = false
            } else if (password.length < 8) {
                passwordError.setText(R.string.password_too_short)
                passwordError.visibility = View.VISIBLE
                isValid = false
            }


            if (!isValid) return@setOnClickListener

            // Firebase Authentication untuk membuat pengguna baru
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Registrasi berhasil, dapatkan UID pengguna
                        val userId = auth.currentUser?.uid
                        val userRef = FirebaseDatabase.getInstance("https://j-go-login-dan-resgister-default-rtdb.firebaseio.com/")
                            .getReference("users")
                            .child(userId!!)

                        // Membuat map untuk menyimpan data pengguna ke Firebase Realtime Database
                        val user = mapOf(
                            "name" to name,
                            "email" to email
                        )

                        // Menyimpan data pengguna di Firebase Realtime Database
                        userRef.setValue(user).addOnCompleteListener { saveTask ->
                            if (saveTask.isSuccessful) {
                                Toast.makeText(this, "Registrasi berhasil!", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this, "Gagal menyimpan data pengguna: ${saveTask.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(this, "Registrasi gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        // Handle Sign In link click
        signInLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
