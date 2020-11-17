package by.sergeantbulkin.cellular.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.sergeantbulkin.cellular.room.model.Abonent

class SharedViewModel : ViewModel()
{
    val temp = MutableLiveData<Int>()
    val abonent = MutableLiveData<Abonent>()
}