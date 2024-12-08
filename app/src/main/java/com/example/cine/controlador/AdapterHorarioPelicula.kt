package com.example.cine.controlador

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cine.R
import com.example.cine.modelo.Horario
import com.example.cine.modelo.Pelicula

class AdapterHorarioPelicula(private val dataSet: ArrayList<Horario>) :
    RecyclerView.Adapter<AdapterHorarioPelicula.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tHorainicio: TextView
        val tHoraFin: TextView
        val btnSeleccionar: Button

        init {
            // Define click listener for the ViewHolder's View
            tHorainicio = view.findViewById(R.id.horaInicioText)
            tHoraFin = view.findViewById(R.id.horaFinText)
            btnSeleccionar = view.findViewById(R.id.seleccionarButton)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.modelo_horario, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.tHorainicio.text = dataSet[position].horaInicio
        viewHolder.tHoraFin.text = dataSet[position].horaFin
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
