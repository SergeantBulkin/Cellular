package by.sergeantbulkin.cellular.ui.abonents

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import by.sergeantbulkin.cellular.R
import by.sergeantbulkin.cellular.room.model.Abonent

class AbonentsAdapter(private val clickListener : (Abonent) -> Unit) : RecyclerView.Adapter<AbonentsAdapter.AbonentViewHolder>()
{
    //----------------------------------------------------------------------------------------------
    private var abonentsList = arrayListOf<Abonent>()
    //----------------------------------------------------------------------------------------------
    fun setAbonents(abonents : ArrayList<Abonent>)
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
    fun addItem(abonent: Abonent)
    {
        abonentsList.add(abonent)
        notifyItemInserted(abonentsList.size-1)
    }
    fun removeItem(abonent: Abonent)
    {
        val index = abonentsList.indexOf(abonent)
        abonentsList.removeAt(index)
        notifyItemRemoved(index)
    }
    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    inner class AbonentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        private var fioTextView : TextView = itemView.findViewById(R.id.item_abonent_fio)
        private var numberTextView : TextView = itemView.findViewById(R.id.item_abonent_number)

        fun bind(abonent: Abonent)
        {
            fioTextView.text = ""
            fioTextView.append("${abonent.lastName} ${abonent.name} ${abonent.middleName}")
            numberTextView.text = abonent.mobileNumber
            //Нажатие обрабатывается в MainActivity
            itemView.setOnClickListener { clickListener(abonent) }
        }
    }
    //----------------------------------------------------------------------------------------------
}