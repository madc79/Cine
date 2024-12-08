package com.example.cine.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cine.MainActivity
import com.example.cine.R
import com.example.cine.controlador.PeliculaDatabaseHelper
import com.example.cine.databinding.ActivityLoginBinding
import com.example.cine.databinding.ActivityRegistroBinding
import com.example.cine.modelo.User

class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding

    private lateinit var correo: EditText
    private lateinit var contrasena: EditText
    private lateinit var registro: Button
    private lateinit var dbHelper: PeliculaDatabaseHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)

        setContentView(binding.root)
        init()
        dbHelper = PeliculaDatabaseHelper(this)

        registro.setOnClickListener(View.OnClickListener {
            var c = correo.text.toString()
            var password = contrasena.text.toString()

            dbHelper.insertUser(User(c, password))
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        })

    }


    private fun init() {
        correo = binding.correoRegistroText
        contrasena = binding.contraseARegistroText
        registro = binding.registrarseButton

    }
}