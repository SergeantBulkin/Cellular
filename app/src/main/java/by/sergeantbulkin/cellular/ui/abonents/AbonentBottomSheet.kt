package by.sergeantbulkin.cellular.ui.abonents

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import by.sergeantbulkin.cellular.Operation
import by.sergeantbulkin.cellular.databinding.FragmentAbonentBinding
import by.sergeantbulkin.cellular.room.AbonentsDatabase
import by.sergeantbulkin.cellular.room.model.Abonent
import by.sergeantbulkin.cellular.room.model.Plan
import by.sergeantbulkin.cellular.room.model.Service
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class AbonentBottomSheet(val submitListener : (Abonent, Operation) -> Unit) : BottomSheetDialogFragment()
{
    //----------------------------------------------------------------------------------------------
    private lateinit var binding : FragmentAbonentBinding
    //Выбранный абонент
    private var abonent: Abonent? = null
    //Выбранный план для абонента
    private var chosenPlan : Int = 0
    private var chosenDate : Long = 0L
    private var abonId : Int = 0
    //CompositeDisposable для хранения подписок
    private val compositeDisposable = CompositeDisposable()
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
            //Изменить кнопки и Title
            binding.abonentButtonAdd.visibility = View.GONE
            binding.abonentButtonUpdate.visibility = View.VISIBLE
            binding.abonentButtonDelete.visibility = View.VISIBLE
            binding.abonentTitle.text = "Абонент"
        }

        //Загрузить необходимые данные
        loadData()

        //Настроить слушателей
        setUpViewListeners()

        //Повесить TextWatchers
        setTextWatchers()
    }
    //----------------------------------------------------------------------------------------------
    private fun fillData()
    {
        //Запомнить имеющиеся данные
        abonId = abonent!!.abonentId
        chosenDate = abonent!!.registrationDate
        chosenPlan = abonent!!.planId

        binding.abonentNameTextInputEditText.setText(abonent?.name, TextView.BufferType.EDITABLE)
        binding.abonentLastnameTextInputEditText.setText(abonent?.lastName, TextView.BufferType.EDITABLE)
        binding.abonentMiddlenameTextInputEditText.setText(abonent?.middleName, TextView.BufferType.EDITABLE)
        when (abonent?.isWoman!!)
        {
            true -> binding.abonentRadioButtonWoman.isChecked = true
            false -> binding.abonentRadioButtonMan.isChecked = true
        }
        binding.abonentAgeTextInputEditText.setText("${abonent?.age}", TextView.BufferType.EDITABLE)
        binding.abonentAddressTextInputEditText.setText(abonent?.address, TextView.BufferType.EDITABLE)
        binding.abonentRegistrationDateTextInputEditText.setText(parseLongToDate(abonent?.registrationDate!!), TextView.BufferType.EDITABLE)
        binding.abonentMobileNumberTextInputEditText.setText(abonent?.mobileNumber, TextView.BufferType.EDITABLE)
    }
    //----------------------------------------------------------------------------------------------
    //Загрузить данные
    private fun loadData()
    {
        //Загрузить тарифные планы
        AbonentsDatabase.INSTANCE?.planDao()
            ?.getPlans()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                setPlansTextViewAdapter(it)
            },{
                Log.d("TAG", "Error: ${it.localizedMessage}")
            }).let {
                if (it != null)
                {
                    compositeDisposable.add(it)
                }
            }
    }
    //----------------------------------------------------------------------------------------------
    private fun setServicesTextViewAdapter(services: List<Service>)
    {
        //Список для установки в AutocompleteTextView
        val serviceNames = arrayListOf<String>()
        for (service in services)
        {
            serviceNames.add(service.name)
        }
        //Установить адаптер в AutocompleteTextView
        val adapter = ArrayAdapter(requireContext(), android.R.layout.select_dialog_item, serviceNames)
        binding.abonentServicesAutocomplete.setAdapter(adapter)
        //Сделать доступным
        binding.abonentServicesTextInputLayout.isEnabled = true
        //Обработка выбора из DropDown
        binding.abonentServicesAutocomplete.setOnItemClickListener { parent, view, position, id ->
            //chosenService = services[position].id
            binding.abonentServicesAutocomplete.clearFocus()
        }
    }
    //----------------------------------------------------------------------------------------------
    private fun setPlansTextViewAdapter(plans: List<Plan>)
    {
        //Список для установки в AutocompleteTextView
        val planNames = arrayListOf<String>()
        Log.d("TAG", "Планы - $plans")
        //План выбранного абонента, если он есть
        var currentPlan = ""
        for (plan in plans)
        {
            planNames.add(plan.name)
            //Сравниваем ID плана с planId выбранного абонента, если абонент выбран
            if (plan.id == chosenPlan)
            {
                currentPlan = plan.name
            }
        }
        Log.d("TAG", "Имена - $planNames")
        //Установить адаптер в AutocompleteTextView
        val adapter = ArrayAdapter(requireContext(), android.R.layout.select_dialog_item, planNames)
        binding.abonentPlanAutocomplete.setAdapter(adapter)
        //Сделать доступным
        binding.abonentPlanTextInputLayout.isEnabled = true
        //Обработка выбора из DropDown
        binding.abonentPlanAutocomplete.setOnItemClickListener { parent, view, position, id ->
            chosenPlan = plans[position].id
            binding.abonentPlanAutocomplete.clearFocus()
        }
        //Установить текущий план
        binding.abonentPlanAutocomplete.setText(currentPlan)
        //Слушатель на начальную иконку
        binding.abonentPlanTextInputLayout.setStartIconOnClickListener { binding.abonentPlanAutocomplete.setText("") }
    }
    //----------------------------------------------------------------------------------------------
    //Настройка слушателей
    private fun setUpViewListeners()
    {
        //Обработка нажатия кнопки "Закрыть"
        binding.abonentArrowDown.setOnClickListener { dismiss() }

        //Слушатель RadioButtonMan
        binding.abonentRadioButtonMan.setOnClickListener {
            binding.abonentRadioButtonWoman.isChecked = false
            binding.abonentRadioButtonWoman.setTextColor(resources.getColor(android.R.color.black, context?.theme))
            binding.abonentRadioButtonMan.setTextColor(resources.getColor(android.R.color.black, context?.theme))
        }

        //Слушатель RadioButtonWoman
        binding.abonentRadioButtonWoman.setOnClickListener{
            binding.abonentRadioButtonMan.isChecked = false
            binding.abonentRadioButtonWoman.setTextColor(resources.getColor(android.R.color.black, context?.theme))
            binding.abonentRadioButtonMan.setTextColor(resources.getColor(android.R.color.black, context?.theme))
        }

        //Слушатель выбора даты
        binding.abonentRegistrationDateTextInputEditText.setOnClickListener {
            //Созздать диалог выбора даты
            val dialog = MaterialDatePicker.Builder.datePicker()
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                .build()
            //Слушатель подтверждения даты
            dialog.addOnPositiveButtonClickListener {
                chosenDate = it
                binding.abonentRegistrationDateTextInputEditText.setText(parseLongToDate(it), TextView.BufferType.EDITABLE)
                binding.abonentRegistrationDateTextInputEditText.clearFocus()
            }
            dialog.show(parentFragmentManager, "datepicker")
        }

        //Слушатели нажатия кнопок
        binding.abonentButtonAdd.setOnClickListener { if(checkInput()) sendResult(Operation.ADD) }
        binding.abonentButtonUpdate.setOnClickListener { if(checkInput()) sendResult(Operation.UPDATE) }
        binding.abonentButtonDelete.setOnClickListener{ submitListener(abonent!!, Operation.DELETE); dismiss() }
    }
    //----------------------------------------------------------------------------------------------
    //Повесить TextWatchers
    private fun setTextWatchers()
    {
        //Имя
        binding.abonentNameTextInputEditText.addTextChangedListener({ _, _, _, _ ->  },{ _, _, _, _ ->
            binding.abonentNameTextInputLayout.error = "" },{})
        //Фамилия
        binding.abonentLastnameTextInputEditText.addTextChangedListener({ _, _, _, _ ->  },{ _, _, _, _ ->
            binding.abonentLastnameTextInputLayout.error = "" },{ })
        //Отчество
        binding.abonentMiddlenameTextInputEditText.addTextChangedListener({ _, _, _, _ ->  },{ _, _, _, _ ->
            binding.abonentMiddlenameTextInputLayout.error = "" },{ })
        //Возраст
        binding.abonentAgeTextInputEditText.addTextChangedListener({ _, _, _, _ ->  },{ _, _, _, _ ->
            binding.abonentAgeTextInputLayout.error = "" },{ })
        //Адрес
        binding.abonentAddressTextInputEditText.addTextChangedListener({ _, _, _, _ ->  },{ _, _, _, _ ->
            binding.abonentAddressTextInputLayout.error = "" },{ })
        //Дата
        binding.abonentRegistrationDateTextInputEditText.addTextChangedListener({ _, _, _, _ ->  },{ _, _, _, _ ->
            binding.abonentRegistrationDateTextInputLayout.error = "" },{ })
        //Номер телефона
        binding.abonentMobileNumberTextInputEditText.addTextChangedListener({ _, _, _, _ ->  },{ _, _, _, _ ->
            binding.abonentMobileNumberTextInputLayout.error = "" },{ })
        //Тарифный план
        binding.abonentPlanAutocomplete.addTextChangedListener({ _, _, _, _ ->  },{ _, _, _, _ ->
            binding.abonentPlanTextInputLayout.error = "" },{ })
    }
    //----------------------------------------------------------------------------------------------
    //Проверка введённых данных
    private fun checkInput() : Boolean
    {
        var flag = true

        //Имя
        if (binding.abonentNameTextInputEditText.text.toString() == "")
        {
            binding.abonentNameTextInputLayout.error = "Введите имя"
            flag = false
        }
        //Фамилия
        if (binding.abonentLastnameTextInputEditText.text.toString() == "")
        {
            binding.abonentLastnameTextInputLayout.error = "Введите фамилию"
            flag = false
        }
        //Отчество
        if ((binding.abonentMiddlenameTextInputEditText.text.toString() == ""))
        {
            binding.abonentMiddlenameTextInputLayout.error = "Введите отчество"
            flag = false
        }
        //Пол
        if (!binding.abonentRadioButtonWoman.isChecked && !binding.abonentRadioButtonMan.isChecked)
        {
            binding.abonentRadioButtonWoman.setTextColor(resources.getColor(android.R.color.holo_red_dark, context?.theme))
            binding.abonentRadioButtonMan.setTextColor(resources.getColor(android.R.color.holo_red_dark, context?.theme))
            flag = false
        }
        //Возраст
        if (binding.abonentAgeTextInputEditText.text.toString() == "")
        {
            binding.abonentAgeTextInputLayout.error = "Введите возраст"
            flag = false
        }
        //Адрес
        if (binding.abonentAddressTextInputEditText.text.toString() == "")
        {
            binding.abonentAddressTextInputLayout.error = "Введите адрес"
            flag = false
        }
        //Дата
        if (binding.abonentRegistrationDateTextInputEditText.text.toString() == "")
        {
            binding.abonentRegistrationDateTextInputLayout.error = "Выберите дату"
            flag = false
        }
        //Номер
        if (binding.abonentMobileNumberTextInputEditText.text.toString() == "")
        {
            binding.abonentMobileNumberTextInputLayout.error = "Введите номер"
            flag = false
        }
        //Тарифный план
        if (binding.abonentPlanAutocomplete.text.toString() == "")
        {
            binding.abonentPlanTextInputLayout.error = "Выберите тариф"
            flag = false
        }

        return flag
    }
    //----------------------------------------------------------------------------------------------
    //Отправить результат в MainActivity
    private fun sendResult(operation: Operation)
    {
        val name = binding.abonentNameTextInputEditText.text.toString()
        val lastName = binding.abonentLastnameTextInputEditText.text.toString()
        val middleName = binding.abonentMiddlenameTextInputEditText.text.toString()
        val isWoman = binding.abonentRadioButtonWoman.isChecked
        val age = binding.abonentAgeTextInputEditText.text.toString().toInt()
        val address = binding.abonentAddressTextInputEditText.text.toString()
        val date = chosenDate
        val number = binding.abonentMobileNumberTextInputEditText.text.toString()
        val planId = chosenPlan

        abonent = Abonent(name, lastName, middleName, isWoman, age, address, date, number, planId)
        //Если Id остался нулём, значит мы не редактируем абонента а создаём нового
        if (abonId != 0)
        {
            abonent!!.abonentId = abonId
        }

        submitListener(abonent!!, operation)
        dismiss()
    }
    //----------------------------------------------------------------------------------------------
    private fun parseLongToDate(lDate : Long) : String
    {
        return SimpleDateFormat("dd.MM.yyyy", Locale.ROOT).format(Date(lDate))
    }
    //----------------------------------------------------------------------------------------------
    override fun onDestroy()
    {
        super.onDestroy()
        compositeDisposable.dispose()
    }
    //----------------------------------------------------------------------------------------------
    companion object
    {
        @JvmStatic
        fun newInstance(abonent: Abonent, submitListener : (Abonent, Operation) -> Unit) =
            AbonentBottomSheet(submitListener).apply {
                arguments = Bundle().apply {
                    putParcelable("abonent", abonent)
                }
            }
    }
    //----------------------------------------------------------------------------------------------
}