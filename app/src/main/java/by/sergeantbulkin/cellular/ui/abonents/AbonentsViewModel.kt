package by.sergeantbulkin.cellular.ui.abonents

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.sergeantbulkin.cellular.DisposableManager
import by.sergeantbulkin.cellular.room.AbonentsDatabase
import by.sergeantbulkin.cellular.room.model.Abonent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AbonentsViewModel : ViewModel()
{
    //----------------------------------------------------------------------------------------------
    var abonentsList = mutableListOf<Abonent>()
    var abonentsListLive = MutableLiveData<List<Abonent>>()
    var dbInstance : AbonentsDatabase? = null
    //----------------------------------------------------------------------------------------------
    fun setDatabase(dbInstance : AbonentsDatabase)
    {
        this.dbInstance = dbInstance
    }
    //----------------------------------------------------------------------------------------------
    //Добавить абонента
    fun addAbonent(abonent: Abonent)
    {
        dbInstance?.abonentDao()?.insertAbonent(abonent)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                {
                    abonentsList.add(abonent)
                    abonentsListLive.postValue(abonentsList)
                },
                {
                    Log.d("TAG", "Error: " + it.localizedMessage)
                }).let {
                if (it != null)
                {
                    DisposableManager.add(it)
                }
            }
    }
    //----------------------------------------------------------------------------------------------
    //Получить всех абонентов
    fun getAllAbonents()
    {
        dbInstance?.abonentDao()?.getAbonents()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                {
                    if (!it.isNullOrEmpty())
                    {
                        //Отправить слушателю
                        abonentsListLive.postValue(it)
                        //Добавить все элементы
                        abonentsList.addAll(it)
                    } else
                    {
                        //Отправить слушателю пустой список
                        abonentsListLive.postValue(listOf())
                    }
                },
                {

                }).let {
                if (it != null)
                {
                    DisposableManager.add(it)
                }
            }
    }
    //----------------------------------------------------------------------------------------------
    override fun onCleared()
    {
        DisposableManager.dispose()
        super.onCleared()
    }
    //----------------------------------------------------------------------------------------------
}