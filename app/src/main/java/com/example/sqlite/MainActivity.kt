package com.example.sqliteapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sqlite.R

class MainActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseHelper = DatabaseHelper(this)

        val etName = findViewById<EditText>(R.id.etName)
        val etPhone = findViewById<EditText>(R.id.etPhone)
        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val btnUpdate = findViewById<Button>(R.id.btnUpdate)
        val btnDelete = findViewById<Button>(R.id.btnDelete)
        val btnView = findViewById<Button>(R.id.btnView)
        val tvResult = findViewById<TextView>(R.id.tvResult)

        btnAdd.setOnClickListener {
            val name = etName.text.toString()
            val phone = etPhone.text.toString()
            if (databaseHelper.insertContact(name, phone)) {
                Toast.makeText(this, "Thêm thành công!", Toast.LENGTH_SHORT).show()
                etName.text.clear()
                etPhone.text.clear()
            } else {
                Toast.makeText(this, "Lỗi khi thêm!", Toast.LENGTH_SHORT).show()
            }
        }

        btnUpdate.setOnClickListener {
            val name = etName.text.toString()
            val phone = etPhone.text.toString()
            if (databaseHelper.updateContact(name, phone)) {
                Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Lỗi khi cập nhật!", Toast.LENGTH_SHORT).show()
            }
        }

        btnDelete.setOnClickListener {
            val name = etName.text.toString()
            if (databaseHelper.deleteContact(name)) {
                Toast.makeText(this, "Xóa thành công!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Lỗi khi xóa!", Toast.LENGTH_SHORT).show()
            }
        }

        btnView.setOnClickListener {
            tvResult.text = databaseHelper.getAllContacts()
        }
    }
}
