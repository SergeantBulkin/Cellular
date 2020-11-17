package by.sergeantbulkin.cellular.ui.abonents

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.sergeantbulkin.cellular.room.AbonentsDatabase
import by.sergeantbulkin.cellular.room.model.Abonent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class AbonentsViewModel : ViewModel()
{
    //----------------------------------------------------------------------------------------------
    private var abonentsList = mutableListOf<Abonent>()
    private val compositeDisposable = CompositeDisposable()
    var abonentsListLive = MutableLiveData<List<Abonent>>()
    //----------------------------------------------------------------------------------------------
    //Добавить абонента
    fun addAbonent(abonent: Abonent)
    {
        AbonentsDatabase.INSTANCE?.abonentDao()?.insertAbonent(abonent)
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
                    compositeDisposable.add(it)
                }
            }
    }
    //----------------------------------------------------------------------------------------------
    //Получить всех абонентов
    fun getAllAbonents()
    {
        AbonentsDatabase.INSTANCE?.abonentDao()?.getAbonents()
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
                        Log.d("TAG", "AbonentsViewModel - getAllAbonents - пустой")
                        //Отправить слушателю пустой список
                        abonentsListLive.postValue(listOf())
                    }
                },
                {
                    Log.d("TAG", "AbonentsViewModel - getAllAbonents - Error: ${it.localizedMessage}")
                }).let {
                if (it != null)
                {
                    compositeDisposable.add(it)
                }
            }
    }
    //----------------------------------------------------------------------------------------------
    override fun onCleared()
    {
        super.onCleared()
        compositeDisposable.dispose()
        compositeDisposable.clear()
    }
    //----------------------------------------------------------------------------------------------
}