package com.example.cine.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.cine.R
import com.example.cine.databinding.FragmentAcercadeBinding
import com.example.cine.databinding.FragmentAjustesBinding

class AcercaFragment : Fragment() {

    private var _binding: FragmentAcercadeBinding? = null
    private val binding get() = _binding!!

    // Definir el nombre del archivo de SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflamos el binding
        _binding = FragmentAcercadeBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Método para limpiar recursos cuando el fragmento se destruye
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
