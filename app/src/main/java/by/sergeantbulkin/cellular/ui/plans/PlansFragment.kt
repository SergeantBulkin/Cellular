package by.sergeantbulkin.cellular.ui.plans

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import by.sergeantbulkin.cellular.R

class PlansFragment : Fragment()
{

    private lateinit var plansViewModel : PlansViewModel

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View?
    {
        plansViewModel = ViewModelProvider(this).get(PlansViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_plans, container, false)
        val textView : TextView = root.findViewById(R.id.text_gallery)
        plansViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}