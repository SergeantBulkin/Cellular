package by.sergeantbulkin.cellular.ui.plans

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import by.sergeantbulkin.cellular.R
import by.sergeantbulkin.cellular.room.model.Abonent
import by.sergeantbulkin.cellular.room.model.Service

class PlanServiceAdapter(private val deleteListener : (Service) -> Unit) : RecyclerView.Adapter<PlanServiceAdapter.PlanServiceViewHolder>()
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
    fun addItems(serviceList: List<Service>)
    {
        for (ser in serviceList)
        {
            services.add(ser)
            notifyItemInserted(services.size-1)
        }
    }
    fun removeItem(service: Service)
    {
        val index = services.indexOf(service)
        services.removeAt(index)
        notifyItemRemoved(index)
    }
    //----------------------------------------------------------------------------------------------
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanServiceViewHolder
    {
        return PlanServiceViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_services_in_plan, parent, false))
    }
    //----------------------------------------------------------------------------------------------
    override fun onBindViewHolder(holder: PlanServiceViewHolder, position: Int)
    {
        holder.bind(services[position])
    }
    //----------------------------------------------------------------------------------------------
    override fun getItemCount(): Int
    {
        return services.size
    }
    //----------------------------------------------------------------------------------------------
    inner class PlanServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        private var serviceNameTextView : TextView = itemView.findViewById(R.id.plan_service_name)
        private var serviceCostTextView : TextView = itemView.findViewById(R.id.plan_service_cost)
        private var serviceDeleteImage : ImageView = itemView.findViewById(R.id.plan_service_delete)

        fun bind(service: Service)
        {
            serviceNameTextView.text = service.name
            serviceCostTextView.text = "${service.cost}"
            //Нажатие обрабатывается в MainActivity
            serviceDeleteImage.setOnClickListener { deleteListener(service) }
        }
    }
    //----------------------------------------------------------------------------------------------
}