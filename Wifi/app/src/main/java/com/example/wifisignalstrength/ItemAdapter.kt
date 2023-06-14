package com.example.wifisignalstrength

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wifisignalstrength.model.AccesPoint

class ItemAdapter(private val list: List<AccesPoint>): RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

//    stores references to vievws in a list item
    class ItemViewHolder(view: View): RecyclerView.ViewHolder(view){
        var ssidView: TextView = itemView.findViewById(R.id.item_ssid)
        val frequencyView: TextView = itemView.findViewById(R.id.item_frequency)
        val rssiView: TextView = itemView.findViewById(R.id.item_rssi)
        val speedlinkView: TextView = itemView.findViewById(R.id.item_speedlink)
        val distanceView: TextView = itemView.findViewById(R.id.item_distance)
    }

//    creates instance of viewHolhder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_view,parent,false)
        return ItemViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return list.size
    }
//    binds data from specific position in a list with viewholders
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = list.get(position)
        holder.ssidView.text = item.ssid
        holder.frequencyView.text = item.frequency
        holder.rssiView.text = item.rssi
        holder.speedlinkView.text = item.speedLink
        holder.distanceView.text = item.distance
    }
}