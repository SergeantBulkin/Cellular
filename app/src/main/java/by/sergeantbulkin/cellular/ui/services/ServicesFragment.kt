package by.sergeantbulkin.cellular.ui.services

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.sergeantbulkin.cellular.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ServicesFragment : BottomSheetDialogFragment()
{
    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View?
    {
        val root = inflater.inflate(R.layout.item_list_abonent, container, false)
        return root
    }
}