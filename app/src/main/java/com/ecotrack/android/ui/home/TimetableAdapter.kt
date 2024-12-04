package com.ecotrack.android.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ecotrack.android.R

class TimetableAdapter(
    private val data: List<List<String>>
) : RecyclerView.Adapter<TimetableAdapter.TimetableViewHolder>() {

    class TimetableViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemTypeText: TextView = view.findViewById(R.id.item_type_text)
        val dayMon: TextView = view.findViewById(R.id.day_mon)
        val dayTue: TextView = view.findViewById(R.id.day_tue)
        val dayWed: TextView = view.findViewById(R.id.day_wed)
        val dayThu: TextView = view.findViewById(R.id.day_thu)
        val dayFri: TextView = view.findViewById(R.id.day_fri)
        val daySat: TextView = view.findViewById(R.id.day_sat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimetableViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_timetable_row, parent, false)
        return TimetableViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimetableViewHolder, position: Int) {
        val row = data[position]
        holder.itemTypeText.text = row[0]
        holder.dayMon.text = row[1]
        holder.dayTue.text = row[2]
        holder.dayWed.text = row[3]
        holder.dayThu.text = row[4]
        holder.dayFri.text = row[5]
        holder.daySat.text = row[6]
    }

    override fun getItemCount(): Int = data.size
}
