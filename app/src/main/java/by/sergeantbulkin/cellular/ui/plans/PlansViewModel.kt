package by.sergeantbulkin.cellular.ui.plans

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class PlansViewModel(application: Application) : AndroidViewModel(application)
{
    private val _text = MutableLiveData<String>().apply {
        value = "This is gallery Fragment"
    }
    val text : LiveData<String> = _text
}