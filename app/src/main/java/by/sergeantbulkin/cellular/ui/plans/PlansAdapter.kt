package by.sergeantbulkin.cellular.ui.plans

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import by.sergeantbulkin.cellular.R
import by.sergeantbulkin.cellular.room.model.PlanInfo

class PlansAdapter(private val clickListener : (PlanInfo) -> Unit) : RecyclerView.Adapter<PlansAdapter.PlanViewHolder>()
{
    //----------------------------------------------------------------------------------------------
    private var plans = arrayListOf<PlanInfo>()
    //----------------------------------------------------------------------------------------------
    fun setPlans(plansReady : List<PlanInfo>)
    {
        if (plans.size == 0)
        {
            this.plans.addAll(plansReady)
            notifyDataSetChanged()
        }
    }
    //----------------------------------------------------------------------------------------------
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder
    {
        return PlanViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_plan, parent, false))
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int)
    {
        holder.bind(plans[position])
    }

    override fun getItemCount(): Int
    {
        return plans.size
    }
    //----------------------------------------------------------------------------------------------
    inner class PlanViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
    {
        private var nameTextView : TextView = itemView.findViewById(R.id.plan_name)
        private var servicesTextView : TextView = itemView.findViewById(R.id.plan_services)
        private var costTextView : TextView = itemView.findViewById(R.id.plan_cost)

        fun bind(planInfo: PlanInfo)
        {
            nameTextView.text = planInfo.planName
            servicesTextView.text = planInfo.servicesToString()
            costTextView.text = "${planInfo.planCost}"
            //Нажатие обрабатывается в MainActivity
            itemView.setOnClickListener { clickListener(planInfo) }
        }
    }
    //----------------------------------------------------------------------------------------------
}