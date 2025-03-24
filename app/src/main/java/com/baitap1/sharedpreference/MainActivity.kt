package com.baitap1.sharedpreference

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    private lateinit var preferenceHelper: PreferenceHelper
    private lateinit var edt_Username: EditText
    private lateinit var edt_Password: EditText
    private lateinit var btn_Save: Button
    private lateinit var btn_Delete: Button
    private lateinit var btn_Show: Button
    private lateinit var txt_Result: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        preferenceHelper = PreferenceHelper(this)

        edt_Username = findViewById(R.id.edt_Username)
        edt_Password = findViewById(R.id.edt_Password)
        btn_Save = findViewById(R.id.btn_Save)
        btn_Delete = findViewById(R.id.btn_Delete)
        btn_Show = findViewById(R.id.btn_Show)
        txt_Result = findViewById(R.id.txt_Result)

        btn_Save.setOnClickListener {
            val username = edt_Username.text.toString()
            val password = edt_Password.text.toString()
            if (username.isNotEmpty() && password.isNotEmpty()) {
                preferenceHelper.saveData(username, password)
                txt_Result.text = "Đã lưu thông tin thành công!"
            } else {
                txt_Result.text = "Vui lòng nhập đầy đủ thông tin!"
            }
        }

        btn_Show.setOnClickListener {
            val (username, password) = preferenceHelper.getData()
            if (username != null && password != null) {
                txt_Result.text = "Tên người dùng: $username\nMật khẩu: $password"
            } else {
                txt_Result.text = "Không có dữ liệu!"
            }
        }

        btn_Delete.setOnClickListener {
            preferenceHelper.clearData()
            txt_Result.text = "Đã xóa dữ liệu!"
        }
    }
}