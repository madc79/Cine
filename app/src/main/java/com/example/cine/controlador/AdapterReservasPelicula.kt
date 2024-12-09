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
import com.example.cine.modelo.HorarioPelicula
import com.example.cine.modelo.Pelicula
import com.example.cine.modelo.ReservaDetallada

class AdapterReservasPelicula(private val dataSet: ArrayList<ReservaDetallada>) :
    RecyclerView.Adapter<AdapterReservasPelicula.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val pelicula: TextView
        val horaInicio: TextView
        val horafin: TextView

        init {
            // Define click listener for the ViewHolder's View
            pelicula = view.findViewById(R.id.peliculaTetx)
            horaInicio = view.findViewById(R.id.textView18)
            horafin = view.findViewById(R.id.horaModeloText)

        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.modelo_diareserva, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.pelicula.text = dataSet[position].titulo
        viewHolder.horaInicio.text = dataSet[position].horaInicio
        viewHolder.horafin.text = dataSet[position].horaFin

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
