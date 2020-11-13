package by.sergeantbulkin.cellular.ui.abonents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import by.sergeantbulkin.cellular.R

class AbonentsFragment : Fragment()
{

    private lateinit var abonentsViewModel : AbonentsViewModel

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View?
    {
        abonentsViewModel = ViewModelProvider(this).get(AbonentsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_abonents, container, false)
        val textView : TextView = root.findViewById(R.id.text_home)
        return root
    }
}