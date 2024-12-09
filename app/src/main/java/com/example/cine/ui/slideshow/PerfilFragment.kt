package com.example.cine.ui.slideshow

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cine.controlador.AdapterHorarioPelicula
import com.example.cine.controlador.AdapterPelicula
import com.example.cine.controlador.AdapterReservasPelicula
import com.example.cine.controlador.NotificationHelper.Companion.sharedPrefFile
import com.example.cine.controlador.PeliculaDatabaseHelper
import com.example.cine.databinding.FragmentHomeBinding
import com.example.cine.databinding.FragmentReservaBinding
import com.example.cine.databinding.FragmentSlideshowBinding

class PerfilFragment : Fragment() {

    private var _binding: FragmentReservaBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var recycler: RecyclerView
    private lateinit var dbHelper: PeliculaDatabaseHelper
    private lateinit var usuario: TextView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReservaBinding.inflate(inflater, container, false)
        val root: View = binding.root
        dbHelper = PeliculaDatabaseHelper(root.context)
        init()

        return root
    }


    private fun init() {
        recycler = binding.horaRecycler
        recycler.layoutManager = LinearLayoutManager(context)
        usuario=binding.usuarioText

        val sharedPreferences = context?.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val correo = sharedPreferences?.getString("user_email", null)

        if (!correo.isNullOrEmpty()) {

            usuario.text=correo
            recycler.adapter =
                AdapterReservasPelicula(dbHelper.getReservasDetalladasPorCorreo(correo))
        } else {
            // Manejar el caso donde el correo no está presente en las preferencias compartidas
            Toast.makeText(context, "Correo no encontrado en preferencias", Toast.LENGTH_SHORT)
                .show()
        }

        usuario = binding.usuarioText
    }


    override fun onResume() {
        super.onResume()
        val root: View = binding.root

        dbHelper = PeliculaDatabaseHelper(root.context)

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}