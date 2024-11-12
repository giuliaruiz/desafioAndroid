package com.example.desafio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.desafio.databinding.ActivityRegisterBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        auth = Firebase.auth
        setContentView(binding.root)


        binding.btnRegister.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val role = binding.spRole.selectedItem.toString() // Corrigido para capturar o item selecionado do Spinner

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        if (user != null) {
                            val db = FirebaseFirestore.getInstance()
                            val userMap = hashMapOf(
                                "email" to email,
                                "funcao" to role,
                                "password" to password
                            )

                            db.collection("user")
                                .add(userMap)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Boa mlk", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener{
                                    Toast.makeText(this, "Mandou mal mlk", Toast.LENGTH_SHORT).show()
                                }
                        }
                    } else {
                        Toast.makeText(baseContext, "Erro ao cadastrar.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}