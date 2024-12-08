package com.example.cine.controlador

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cine.R
import com.example.cine.modelo.Pelicula

class AdapterPelicula(private val dataSet: ArrayList<Pelicula>) :
    RecyclerView.Adapter<AdapterPelicula.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tituloModelo: TextView
        val descripcionModelo: TextView
        val imagenPeliculaModelo: ImageView
        val fondoPelicula: ConstraintLayout

        init {
            // Define click listener for the ViewHolder's View
            tituloModelo = view.findViewById(R.id.tituloModelo)
            descripcionModelo = view.findViewById(R.id.descripcionModelo)
            imagenPeliculaModelo = view.findViewById(R.id.imagenPeliculaModelo)
            fondoPelicula = view.findViewById(R.id.fondoPelicula)
        }
    }


    lateinit var context: Context

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.modelo_pelicula, viewGroup, false)

        context = view.context
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.tituloModelo.text = dataSet[position].tituloPelicula
        viewHolder.descripcionModelo.text = dataSet[position].descripcionPelicula
        Glide.with(context)
            .load(dataSet[position].imagenPelicula)  // Aquí usas la URL o el recurso de la imagen
            .into(viewHolder.imagenPeliculaModelo)
        val currentPosition = position

        viewHolder.fondoPelicula.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("peli", dataSet[currentPosition])
            }

            Utils.nav.navigate(R.id.nav_detalle_pelicula, bundle)
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
