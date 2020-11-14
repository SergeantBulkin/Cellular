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
import by.sergeantbulkin.cellular.room.AbonentsDatabase
import by.sergeantbulkin.cellular.room.model.Abonent
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
        Log.d("TAG", "onCreateView $this")
        //Инициализация ViewModel
        viewModel = ViewModelProvider(this).get(AbonentsViewModel::class.java)
        adapter = AbonentsAdapter()
        binding = FragmentAbonentsBinding.inflate(inflater, container, false)
        return binding.root
    }
    //----------------------------------------------------------------------------------------------
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        Log.d("TAG", "onViewCreated")

        //Установить FAB
        binding.fab.setOnClickListener {
            val abonent = Abonent(name = "Name", lastName = "Lastname", middleName = "Middlename", sex = true, age = 17,
                address = "address", registrationDate = "date", mobileNumber = "+375448674967", planName = "Lemon", planId = 52)
            viewModel.addAbonent(abonent)
        }

        //Настройка RecyclerView
        setUpRecyclerView()

        //База данных
        viewModel.setDatabase(AbonentsDatabase.getAbonentsDatabase(requireContext())!!)

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
        viewModel.abonentsListLive.observe(viewLifecycleOwner,
        {
            if (!it.isNullOrEmpty())
            {
                //Сделать список видимым
                binding.abonentsRecyclerView.visibility = View.VISIBLE
                //Установить новый список в адаптер
                adapter.abonentsList = it as MutableList<Abonent>
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
    override fun onPause()
    {
        super.onPause()
        Log.d("TAG", "onPause")
    }
    override fun onResume()
    {
        super.onResume()
        Log.d("TAG", "onResume")
    }

    override fun onStop()
    {
        super.onStop()
        Log.d("TAG", "onStop")
    }

    override fun onStart()
    {
        super.onStart()
        Log.d("TAG", "onStart")
    }

    override fun onDestroyView()
    {
        super.onDestroyView()
        Log.d("TAG", "onDestroyView")
    }

    override fun onDestroy()
    {
        super.onDestroy()
        Log.d("TAG", "onDestroy")
    }
}