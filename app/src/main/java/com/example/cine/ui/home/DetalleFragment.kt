package com.example.cine.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cine.R
import com.example.cine.controlador.AdapterHorarioPelicula
import com.example.cine.controlador.AdapterReservasPelicula
import com.example.cine.controlador.NotificationHelper
import com.example.cine.controlador.NotificationHelper.Companion.sharedPrefFile
import com.example.cine.controlador.Utils
import com.example.cine.databinding.FragmentPeliculaDetalleBinding
import com.example.cine.modelo.Horario
import com.example.cine.modelo.Pelicula

class DetalleFragment : Fragment() {

    private var _binding: FragmentPeliculaDetalleBinding? = null
    private val binding get() = _binding!!
    private lateinit var recycler: RecyclerView
    private var pelicula: Pelicula? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflamos el binding
        _binding = FragmentPeliculaDetalleBinding.inflate(inflater, container, false)

        // Recuperamos la película del Bundle
        pelicula = arguments?.getSerializable("peli") as? Pelicula

        // Configuramos el RecyclerView
        recycler = binding.horarioPeliculaRecycler
        recycler.layoutManager = LinearLayoutManager(context)

        val sharedPreferences = context?.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val correo = sharedPreferences?.getString("user_email", null)

        if (!correo.isNullOrEmpty()) {
            recycler.adapter =
                pelicula?.let {
                    AdapterHorarioPelicula(
                        it.horario,
                        pelicula!!.tituloPelicula,
                        correo
                    )
                }
        } else {
            // Manejar el caso donde el correo no está presente en las preferencias compartidas
            Toast.makeText(context, "Correo no encontrado en preferencias", Toast.LENGTH_SHORT)
                .show()
        }


        // Asignamos los datos de la película a los TextViews
        pelicula?.let {
            binding.TituloPeliText.text = it.tituloPelicula
            binding.detallePeliText.text = it.descripcionPelicula
            Glide.with(binding.root.context)
                .load(it.imagenPelicula)  // Aquí usas la URL o el recurso de la imagen
                .into(binding.imagenPeliText)
        }

        // Inicializamos el NotificationHelper con requireContext()


        return binding.root
    }


    // Método para limpiar recursos cuando el fragmento se destruye
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
