package com.example.cine.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cine.controlador.AdapterPelicula
import com.example.cine.controlador.PeliculaDatabaseHelper
import com.example.cine.databinding.FragmentHomeBinding
import com.example.cine.modelo.Pelicula

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var recycler: RecyclerView
    private lateinit var dbHelper: PeliculaDatabaseHelper


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        recycler = binding.recyclerViewPeliculas
        recycler.layoutManager = LinearLayoutManager(context)
        val root: View = binding.root
        dbHelper = PeliculaDatabaseHelper(root.context)

        recycler.adapter = AdapterPelicula(dbHelper.getPeliculas())


        return root
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