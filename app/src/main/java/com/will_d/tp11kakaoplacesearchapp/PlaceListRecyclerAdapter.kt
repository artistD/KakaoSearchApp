package com.will_d.tp11kakaoplacesearchapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class PlaceListRecyclerAdapter(val context:Context, val places:ArrayList<Place>) : RecyclerView.Adapter<PlaceListRecyclerAdapter.VH>(){
    //생성자 - 객체가 new 될때 자동으로 실행되는 메소드

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvPlaceName: TextView by lazy { itemView.findViewById(R.id.tv_place_name) }
        val tvAddress : TextView by lazy { itemView.findViewById(R.id.tv_address) }
        val tvDistance : TextView by lazy { itemView.findViewById(R.id.tv_distance) }

        init {
            itemView.setOnClickListener {
                val place = places.get(adapterPosition)
                val intent : Intent  = Intent(context, PlaceUrlActivity::class.java)
                intent.putExtra("placeUrl", place.place_url)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val itemView :View = LayoutInflater.from(context).inflate(R.layout.recycler_item_list_fragment, parent, false)
        return VH(itemView)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val place : Place = places.get(position)
        holder.tvAddress.text = place.address_name
        holder.tvPlaceName.text = place.place_name
        holder.tvDistance.text = place.distance
    }

    override fun getItemCount(): Int {
        return places.size
    }
}