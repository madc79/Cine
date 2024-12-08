package com.example.cine.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cine.controlador.PeliculaDatabaseHelper
import com.example.cine.databinding.FragmentAltaBinding
import com.example.cine.modelo.Horario
import com.example.cine.modelo.Pelicula

class AltaFragment : Fragment() {

    private var _binding: FragmentAltaBinding? = null
    private lateinit var dbHelper: PeliculaDatabaseHelper
    private lateinit var descripcion: EditText
    private lateinit var horaInicio: EditText
    private lateinit var horaFin: EditText
    private lateinit var titulo: EditText
    private lateinit var imagen: EditText
    private lateinit var tresD: CheckBox
    private lateinit var edad: EditText
    private lateinit var enviar: Button


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAltaBinding.inflate(inflater, container, false)
        val root: View = binding.root
        dbHelper = PeliculaDatabaseHelper(root.context)
        init()


        enviar.setOnClickListener {
            val horasInicio = horaInicio.text.toString().split(";")
            val horasFin = horaFin.text.toString().split(";")


            val horario = ArrayList<Horario>()


            for ((indice, elemento) in horasInicio.withIndex()) {
                horario.add(Horario(horasInicio.get(indice), horasFin.get(indice)))

            }


            val pelicula = Pelicula(
                tituloPelicula = titulo.text.toString(),
                descripcionPelicula = descripcion.text.toString(),
                imagenPelicula = imagen.text.toString(),
                horario = horario,
                tresD = tresD.isChecked,
                restriccionDeEdad = edad.text.toString()
            )
            val success = dbHelper.insertPelicula(pelicula)
            if (success > 0) {
                Toast.makeText(
                    requireContext(),
                    "Película insertada correctamente",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Error al insertar la película",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }




        return root
    }


    private fun init() {
        descripcion = binding.descaripcionText
        horaInicio = binding.descaripcionText2
        horaFin = binding.horaText
        titulo = binding.nombreText2
        imagen = binding.nombreText
        tresD = binding.tresDCheck
        edad = binding.restrccionEdadText
        enviar = binding.enviarReservaButton
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}