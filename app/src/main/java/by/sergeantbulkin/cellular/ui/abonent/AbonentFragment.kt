package by.sergeantbulkin.cellular.ui.abonent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.sergeantbulkin.cellular.databinding.FragmentAbonentBinding
import by.sergeantbulkin.cellular.room.model.Abonent
import by.sergeantbulkin.cellular.ui.abonents.AbonentsViewModel

class AbonentFragment : Fragment()
{
    //----------------------------------------------------------------------------------------------
    //Выбранный абонент
    private var abonent: Abonent? = null
    //ViewModel
    private lateinit var viewModel: AbonentsViewModel
    //Binding
    private lateinit var binding: FragmentAbonentBinding
    //----------------------------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        arguments?.let {
            abonent = it.getParcelable("abonent")
        }
    }
    //----------------------------------------------------------------------------------------------
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        //Инициализация ViewModel
        viewModel = ViewModelProvider(this).get(AbonentsViewModel::class.java)
        //Инициализация переменной биднинга
        binding = FragmentAbonentBinding.inflate(inflater, container, false)
        return binding.root
    }
    //----------------------------------------------------------------------------------------------
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        //Если не null, значит редактируем информацию
        if (abonent != null)
        {
            fillData()
            //Изменить кнопки
            binding.abonentButtonAdd.visibility = View.GONE
            binding.abonentButtonEdit.visibility = View.VISIBLE
            binding.abonentButtonDelete.visibility = View.VISIBLE
        }

        //Настроить слушателей
        setUpViewListeners()
    }
    //----------------------------------------------------------------------------------------------
    private fun fillData()
    {
        binding.abonentNameTextInputEditText.setText(abonent?.name, TextView.BufferType.EDITABLE)
        binding.abonentLastnameTextInputEditText.setText(abonent?.lastName, TextView.BufferType.EDITABLE)
        binding.abonentMiddlenameTextInputEditText.setText(abonent?.middleName, TextView.BufferType.EDITABLE)
        when (abonent?.isWoman!!)
        {
            true -> binding.abonentRadioButtonWoman.isChecked = true
            false -> binding.abonentRadioButtonMan.isChecked = true
        }
        abonent?.age?.let {binding.abonentAgeTextInputEditText.setText("$it", TextView.BufferType.EDITABLE)}
        binding.abonentAddressTextInputEditText.setText(abonent?.address, TextView.BufferType.EDITABLE)
        binding.abonentRegistrationDateTextInputEditText.setText(abonent?.registrationDate, TextView.BufferType.EDITABLE)
        binding.abonentMobileNumberTextInputEditText.setText(abonent?.mobileNumber, TextView.BufferType.EDITABLE)
        binding.abonentPlanTextInputEditText.setText(abonent?.planName, TextView.BufferType.EDITABLE)

    }
    //----------------------------------------------------------------------------------------------
    private fun setUpViewListeners()
    {
        //Слушатель RadioButtonMan
        binding.abonentRadioButtonMan.setOnClickListener {
            binding.abonentRadioButtonWoman.isChecked = false
        }

        //Слушатель RadioButtonWoman
        binding.abonentRadioButtonWoman.setOnClickListener{
            binding.abonentRadioButtonMan.isChecked = false
        }
    }
    //----------------------------------------------------------------------------------------------
    companion object
    {
        @JvmStatic
        fun newInstance(abonent: Abonent) =
            AbonentFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("abonent", abonent)
                }
            }
    }
    //----------------------------------------------------------------------------------------------
}