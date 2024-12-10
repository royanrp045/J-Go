package com.example.j_go.database

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.j_go.databinding.ItemPlaceBinding

class WisataAdapter(private val places: List<Wisata>) : RecyclerView.Adapter<WisataAdapter.PlaceViewHolder>() {

    class PlaceViewHolder(val binding: ItemPlaceBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val binding = ItemPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = places[position]
        holder.binding.textName.text = place.name
        holder.binding.textCategory.text = place.category
        holder.binding.textPrice.text = "Rp ${place.ticket_price}"
        holder.binding.textRate.text = "Rating: ${place.rate}"
        holder.binding.imagePlace.setImageResource(
            holder.itemView.context.resources.getIdentifier(
                place.image_res, "drawable", holder.itemView.context.packageName
            )
        )
    }

    override fun getItemCount() = places.size
}
