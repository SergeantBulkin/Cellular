package by.sergeantbulkin.cellular.ui.abonents

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import by.sergeantbulkin.cellular.databinding.FragmentAbonentsBinding
import by.sergeantbulkin.cellular.room.model.Abonent
import by.sergeantbulkin.cellular.ui.SharedViewModel
import com.google.android.material.snackbar.Snackbar

class AbonentsFragment : Fragment()
{
    //----------------------------------------------------------------------------------------------
    //ViewModel
    private lateinit var viewModel : AbonentsViewModel
    //Binding
    private lateinit var binding: FragmentAbonentsBinding
    //RecyclerViewAdapter
    private lateinit var adapter : AbonentsAdapter
    //----------------------------------------------------------------------------------------------
    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View?
    {
        //Инициализация ViewModel
        viewModel = ViewModelProvider(this).get(AbonentsViewModel::class.java)
        //Инициализация адаптера
        adapter = AbonentsAdapter{abonent ->
            activity?.let {
                val sharedViewModel = ViewModelProvider(it).get(SharedViewModel::class.java)
                sharedViewModel.abonent.postValue(abonent)
            }
        }
        //Инициализация переменной биднинга
        binding = FragmentAbonentsBinding.inflate(inflater, container, false)
        return binding.root
    }
    //----------------------------------------------------------------------------------------------
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        //Установить FAB
        binding.fab.setOnClickListener {
            //val abonent = Abonent(name = "Name", lastName = "Lastname", middleName = "Middlename", sex = true, age = 17, address = "address", registrationDate = "date", mobileNumber = "+375448674967", planName = "Lemon", planId = 52)
            //viewModel.addAbonent(abonent)

            activity?.let {
                val sharedViewModel = ViewModelProvider(it).get(SharedViewModel::class.java)
                sharedViewModel.temp.postValue(1)
            }

            //Navigation.findNavController(it).navigate(R.id.action_nav_abonents_to_abonentFragment)
        }

        //Настройка RecyclerView
        setUpRecyclerView()

        //Слушать список абонентов
        observerViewModel()

        //Получить абонентов
        viewModel.getAllAbonents()
    }
    //----------------------------------------------------------------------------------------------
    //Настройка RecyclerView
    private fun setUpRecyclerView()
    {
        binding.abonentsRecyclerView.layoutManager = LinearLayoutManager(context)
        //Установить адаптер
        binding.abonentsRecyclerView.adapter = adapter
    }
    //----------------------------------------------------------------------------------------------
    //Подписаться на обновления списка
    private fun observerViewModel()
    {
        //Подписаться на изменения списка абонентов
        viewModel.abonentsListLive.observe(this.viewLifecycleOwner,
        {
            if (!it.isNullOrEmpty())
            {
                //Сделать список видимым
                binding.abonentsRecyclerView.visibility = View.VISIBLE
                //Установить новый список в адаптер
                adapter.setAbonents(it as ArrayList<Abonent>)
            } else
            {
                //Спрятать список
                binding.abonentsRecyclerView.visibility = View.GONE
                //Показать Snackbar с сообщением
                Snackbar.make(binding.root, "Пусто", Snackbar.LENGTH_SHORT).show()
            }
        })
    }
    //----------------------------------------------------------------------------------------------
}