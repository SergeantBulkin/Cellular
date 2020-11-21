package by.sergeantbulkin.cellular.ui.services

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import by.sergeantbulkin.cellular.Operation
import by.sergeantbulkin.cellular.databinding.FragmentServiceBinding
import by.sergeantbulkin.cellular.room.model.Service
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.reactivex.disposables.CompositeDisposable

class ServiceBottomSheet(val submitListener : (Service, Operation) -> Unit) : BottomSheetDialogFragment()
{
    //----------------------------------------------------------------------------------------------
    private lateinit var binding: FragmentServiceBinding
    //Выбранный тарифный план
    private var service : Service? = null
    //ID выбранной услуги
    private var serviceId = 0
    //CompositeDisposable для хранения подписок
    private val compositeDisposable = CompositeDisposable()
    //----------------------------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        arguments?.let {
            service = it.getParcelable("service")
        }
    }
    //----------------------------------------------------------------------------------------------
    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View?
    {
        binding = FragmentServiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    //----------------------------------------------------------------------------------------------
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        //Если не null, значит редактируем информацию
        if (service != null)
        {
            fillData()
            //Изменить кнопки и Title
            binding.serviceButtonAdd.visibility = View.GONE
            binding.serviceButtonUpdate.visibility = View.VISIBLE
            binding.serviceButtonDelete.visibility = View.VISIBLE
            binding.serviceTitle.text = "Редактировать услугу"
        }

        //Настроить слушателей
        setUpViewListeners()

        //Повесить TextWatchers
        setTextWatchers()
    }
    //----------------------------------------------------------------------------------------------
    private fun fillData()
    {
        //Запомнить
        serviceId = service!!.id

        binding.serviceNameTextInputEditText.setText(service!!.name, TextView.BufferType.EDITABLE)
        binding.serviceCostTextInputEditText.setText("${service!!.cost}", TextView.BufferType.EDITABLE)
    }
    //----------------------------------------------------------------------------------------------
    //Настроить слушателей
    private fun setUpViewListeners()
    {
        //Обработка нажатия кнопки "Закрыть"
        binding.serviceArrowDown.setOnClickListener { dismiss() }

        //Слушатели нажатия кнопок
        binding.serviceButtonAdd.setOnClickListener { if(checkInput()) sendResult(Operation.ADD) }
        binding.serviceButtonUpdate.setOnClickListener { if(checkInput()) sendResult(Operation.UPDATE) }
        binding.serviceButtonDelete.setOnClickListener{ submitListener(service!!, Operation.DELETE); dismiss() }
    }
    //----------------------------------------------------------------------------------------------
    //Повесить TextWatchers
    private fun setTextWatchers()
    {
        //Название
        binding.serviceNameTextInputEditText.addTextChangedListener({ _, _, _, _ ->  },{ _, _, _, _ ->
            binding.serviceNameTextInputLayout.error = "" },{})
        //Стоимость
        binding.serviceCostTextInputEditText.addTextChangedListener({ _, _, _, _ ->  },{ _, _, _, _ ->
            binding.serviceCostTextInputLayout.error = "" },{})
    }
    //----------------------------------------------------------------------------------------------
    //Проверка введённых данных
    private fun checkInput() : Boolean
    {
        var flag = true

        //Название
        if (binding.serviceNameTextInputEditText.text.toString() == "")
        {
            binding.serviceNameTextInputLayout.error = "Введите название"
            flag = false
        }

        //Стоимость
        if (binding.serviceCostTextInputEditText.text.toString() == "")
        {
            binding.serviceCostTextInputLayout.error = "Введите название"
            flag = false
        }

        return flag
    }
    //----------------------------------------------------------------------------------------------
    //Отправить результат в MainActivity
    private fun sendResult(operation: Operation)
    {
        val name = binding.serviceNameTextInputEditText.text.toString()
        val cost = binding.serviceCostTextInputEditText.text.toString().toFloat()

        service = Service(name, cost)
        service!!.id = serviceId

        submitListener(service!!, operation)
        dismiss()
    }
    //----------------------------------------------------------------------------------------------
    companion object
    {
        @JvmStatic
        fun newInstance(service: Service, submitListener : (Service, Operation) -> Unit) =
            ServiceBottomSheet(submitListener).apply {
                arguments = Bundle().apply {
                    putParcelable("service", service)
                }
            }
    }
    //----------------------------------------------------------------------------------------------
}