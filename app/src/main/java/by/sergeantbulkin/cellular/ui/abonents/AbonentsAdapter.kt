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
    private var abonents = arrayListOf<Abonent>()
    //----------------------------------------------------------------------------------------------
    fun setAbonents(abonentsList : List<Abonent>)
    {
        if (abonents.size == 0)
        {
            this.abonents.addAll(abonentsList)
            notifyDataSetChanged()
        }
    }
    //----------------------------------------------------------------------------------------------
    override fun getItemCount(): Int
    {
        return abonents.size
    }
    //----------------------------------------------------------------------------------------------
    fun addItem(abonent: Abonent)
    {
        abonents.add(abonent)
        notifyItemInserted(abonents.size-1)
    }
    fun removeItem(abonent: Abonent)
    {
        val index = abonents.indexOf(abonent)
        abonents.removeAt(index)
        notifyItemRemoved(index)
    }
    fun updateItem(abonent: Abonent)
    {
        var index = 0
        for (ab in abonents)
        {
            if (ab.abonentId == abonent.abonentId)
            {
                index = abonents.indexOf(ab)
            }
        }
        abonents[index] = abonent
        notifyItemChanged(index)
    }
    //----------------------------------------------------------------------------------------------
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbonentViewHolder
    {
        return AbonentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_abonent, parent, false))
    }
    //----------------------------------------------------------------------------------------------
    override fun onBindViewHolder(holder: AbonentViewHolder, position: Int)
    {
        holder.bind(abonents[position])
    }
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