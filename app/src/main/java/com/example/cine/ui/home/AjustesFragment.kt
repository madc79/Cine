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
import com.example.cine.databinding.FragmentAjustesBinding

class AjustesFragment : Fragment() {

    private var _binding: FragmentAjustesBinding? = null
    private val binding get() = _binding!!
    private lateinit var switchnotificacion: Switch
    private lateinit var switchPredeterminado: Switch
    private lateinit var switchModoOscuro: Switch
    private lateinit var TextCorreo: EditText
    private lateinit var ButtonCorreo: Button

    // Definir el nombre del archivo de SharedPreferences
    private val sharedPrefFile = "com.example.cine.preferences"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflamos el binding
        _binding = FragmentAjustesBinding.inflate(inflater, container, false)
        init()
        loadSwitchStates()

        // Configurar los listeners para los switches
        switchnotificacion.setOnCheckedChangeListener { _, isChecked ->
            saveSwitchState("notificacion", isChecked)
        }
        switchPredeterminado.setOnCheckedChangeListener { _, isChecked ->
            saveSwitchState("predeterminado", isChecked)
            if (isChecked) {
                val currentNightMode =
                    resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
                when (currentNightMode) {
                    android.content.res.Configuration.UI_MODE_NIGHT_YES -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }

                    android.content.res.Configuration.UI_MODE_NIGHT_NO -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                }
            }


            if (switchPredeterminado.isChecked) {
                switchModoOscuro.isEnabled = false;
            } else {
                switchModoOscuro.isEnabled = true;
            }
        }
        switchModoOscuro.setOnCheckedChangeListener { _, isChecked ->
            saveSwitchState("modoOscuro", isChecked)
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }


        if (switchPredeterminado.isChecked) {
            switchModoOscuro.isEnabled = false;
        } else {
            switchModoOscuro.isEnabled = true;

        }
        binding.contactarButton.setOnClickListener {
            // Aquí puedes implementar la lógica para cuando se presione el botón
        }

        return binding.root
    }

    private fun init() {
        switchPredeterminado = binding.oscuroPredeterminadosCheck
        switchnotificacion = binding.notificacionesCheck
        switchModoOscuro = binding.claroOscuroCheck
        TextCorreo = binding.correoText
        ButtonCorreo = binding.contactarButton
    }

    private fun loadSwitchStates() {
        // Leer los valores guardados en SharedPreferences
        val sharedPreferences =
            requireActivity().getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val notificacion = sharedPreferences.getBoolean("notificacion", false)
        val predeterminado = sharedPreferences.getBoolean("predeterminado", false)
        val modoOscuro = sharedPreferences.getBoolean("modoOscuro", false)

        // Establecer el estado de los Switches según los valores leídos
        switchnotificacion.isChecked = notificacion
        switchPredeterminado.isChecked = predeterminado
        switchModoOscuro.isChecked = modoOscuro
    }

    // Función para guardar el estado de un Switch en SharedPreferences
    private fun saveSwitchState(key: String, value: Boolean) {
        val sharedPreferences =
            requireActivity().getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    // Método para limpiar recursos cuando el fragmento se destruye
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
