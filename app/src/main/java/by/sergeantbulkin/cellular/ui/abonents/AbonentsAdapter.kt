package by.sergeantbulkin.cellular.ui.abonents

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import by.sergeantbulkin.cellular.R
import by.sergeantbulkin.cellular.room.model.Abonent

class AbonentsAdapter : RecyclerView.Adapter<AbonentsAdapter.AbonentViewHolder>()
{
    //----------------------------------------------------------------------------------------------
    var abonentsList = emptyList<Abonent>()
    fun setAbonents(abonents : List<Abonent>)
    {
        this.abonentsList = abonents
        notifyDataSetChanged()
    }
    //----------------------------------------------------------------------------------------------
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbonentViewHolder
    {
        return AbonentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_abonent, parent, false))
    }
    //----------------------------------------------------------------------------------------------
    override fun onBindViewHolder(holder: AbonentViewHolder, position: Int)
    {
        holder.bind(abonentsList[position])
    }
    //----------------------------------------------------------------------------------------------
    override fun getItemCount(): Int
    {
        return abonentsList.size
    }
    //----------------------------------------------------------------------------------------------
    inner class AbonentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        private var fioTextView : TextView? = null
        private var numberTextView : TextView? = null

        init
        {
            fioTextView = itemView.findViewById(R.id.item_abonent_fio)
            numberTextView = itemView.findViewById(R.id.item_abonent_number)
        }

        fun bind(abonent: Abonent)
        {
            fioTextView?.text = ""
            fioTextView?.append("${abonent.lastName} ${abonent.name} ${abonent.middleName}")
            numberTextView?.text = abonent.mobileNumber
        }
    }
    //----------------------------------------------------------------------------------------------
}