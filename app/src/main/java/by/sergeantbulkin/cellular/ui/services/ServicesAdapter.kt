package by.sergeantbulkin.cellular.ui.services

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import by.sergeantbulkin.cellular.R
import by.sergeantbulkin.cellular.room.model.Service

class ServicesAdapter(private val clickListener : (Service) -> Unit) : RecyclerView.Adapter<ServicesAdapter.ServiceViewHolder>()
{
    //----------------------------------------------------------------------------------------------
    private var services = arrayListOf<Service>()
    //----------------------------------------------------------------------------------------------
    fun setServices(serviceList : List<Service>)
    {
        services.clear()
        services.addAll(serviceList)
        notifyDataSetChanged()

    }
    //----------------------------------------------------------------------------------------------
    fun addItem(service: Service)
    {
        services.add(service)
        notifyItemInserted(services.size-1)
    }
    fun removeItem(service: Service)
    {
        val index = services.indexOf(service)
        services.removeAt(index)
        notifyItemRemoved(index)
    }
    fun updateItem(service: Service)
    {
        var index = 0
        for (ind in services.indices)
        {
            if (services[ind].id == service.id)
            {
                index = ind
            }
        }
        services[index] = service
        notifyItemChanged(index)
    }
    //----------------------------------------------------------------------------------------------
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder
    {
        return ServiceViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_services, parent, false))
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int)
    {
        holder.bind(services[position])
    }

    override fun getItemCount(): Int = services.size
    //----------------------------------------------------------------------------------------------
    inner class ServiceViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
    {
        private var nameTextView : TextView = itemView.findViewById(R.id.item_service_name)
        private var costTextView : TextView = itemView.findViewById(R.id.item_service_cost)

        fun bind(service: Service)
        {
            nameTextView.text = service.name
            costTextView.text = "${service.cost}"
            //Нажатие обрабатывается в MainActivity
            itemView.setOnClickListener { clickListener(service) }
        }
    }
    //----------------------------------------------------------------------------------------------
}