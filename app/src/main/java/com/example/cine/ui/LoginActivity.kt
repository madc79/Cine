package com.example.cine.ui

import android.content.Context
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
import com.example.cine.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding


    private lateinit var correo: EditText
    private lateinit var contrasena: EditText
    private lateinit var login: Button
    private lateinit var registrarse: Button


    private lateinit var dbHelper: PeliculaDatabaseHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        init()
        dbHelper = PeliculaDatabaseHelper(this)

        login.setOnClickListener(View.OnClickListener {
            var c = correo.text.toString()
            var password = contrasena.text.toString()

            if (dbHelper.validateUser(c, password)) {
                // Guardar correo y contraseña en SharedPreferences
                val sharedPreferences =
                    getSharedPreferences("com.example.cine.preferences", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()

                editor.putString("user_email", c)
                editor.putString("user_password", password)
                editor.apply()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

        })

        registrarse.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }


    }

    private fun init() {
        correo = binding.correoLoginText
        contrasena = binding.contrasenaText
        login = binding.iniciarSesionButton
        registrarse = binding.registrarseLoginButton

    }
}