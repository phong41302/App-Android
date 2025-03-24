package com.example.firebaseauthapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase






class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var btnLogin: Button
    private lateinit var btnShowData: Button
    private lateinit var tvUserData: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnRegister = findViewById(R.id.btnRegister)
        btnLogin = findViewById(R.id.btnLogin)
        btnShowData = findViewById(R.id.btnShowData)
        tvUserData = findViewById(R.id.tvUserData)

        btnRegister.setOnClickListener { registerUser() }
        btnLogin.setOnClickListener { loginUser() }
        btnShowData.setOnClickListener { showUserData() }
    }

    private fun registerUser() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    saveUserData(email)
                    Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Lỗi: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun loginUser() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Lỗi: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun saveUserData(email: String) {
        val uid = auth.currentUser?.uid ?: return
        val database = FirebaseDatabase.getInstance().getReference("Users")
        val userData = mapOf("email" to email)

        database.child(uid).setValue(userData)
    }

    private fun showUserData() {
        val uid = auth.currentUser?.uid ?: return
        val database = FirebaseDatabase.getInstance().getReference("Users").child(uid)

        database.get().addOnSuccessListener { snapshot ->
            val email = snapshot.child("email").value.toString()
            tvUserData.text = "Email: $email"
        }.addOnFailureListener {
            Toast.makeText(this, "Lỗi khi lấy dữ liệu", Toast.LENGTH_SHORT).show()
        }
    }


}
