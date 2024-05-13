package com.example.v3_pub

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterScreen : Fragment() {
    private lateinit var auth: FirebaseAuth

    // Reference to Firebase instance and node for users
    private val db = FirebaseDatabase.getInstance("https://reviewpub-75274-default-rtdb.europe-west1.firebasedatabase.app")
    private val database = db.getReference("users")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_registration, container, false)

        auth = FirebaseAuth.getInstance()

        // Retrieve input references
        val nameInput = view.findViewById<EditText>(R.id.name_input)
        val emailInput = view.findViewById<EditText>(R.id.email_input)
        val passwordInput = view.findViewById<EditText>(R.id.password_input)
        val confirmPasswordInput = view.findViewById<EditText>(R.id.confirm_password_input)
        val registerButton = view.findViewById<Button>(R.id.register_button)
        val loginButton = view.findViewById<Button>(R.id.login_button)

        // Register button
        registerButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString()
            val confirmPassword = confirmPasswordInput.text.toString()

            // Validate inputs
            if (password != confirmPassword) {
                Toast.makeText(context, "Passwords do not match", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (name.isEmpty()) {
                Toast.makeText(context, "Name cannot be empty", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Register user
            registerUser(name, email, password)
        }

        // Switch to login screen
        loginButton.setOnClickListener {
            findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
        }

        return view
    }

    private fun registerUser(name: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = task.result?.user?.uid
                    if (userId != null) {
                        val userRef = database.child(userId)
                        userRef.setValue(User(userId, name, email))
                    } else {
                        Toast.makeText(
                            context,
                            "User ID is null. Data won't be saved.",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    // store user data under in my firebase schema
                    if (userId != null) {
                        val userRef = database.child(userId)
                        userRef.setValue(User(userId, name, email))
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.i("RegisterScreen", "User data saved successfully.")
                                } else {
                                    Log.e("RegisterScreen", "Failed to save user data: ${task.exception?.message}", task.exception)
                                }
                            }
                    }

                    //  go    to the home fragment after a successful registration
                    findNavController().navigate(R.id.action_registrationFragment_to_homeFragment)
                } else {
                    Toast.makeText(
                        context,
                        "Registration failed: ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}