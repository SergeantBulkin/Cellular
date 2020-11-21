package by.sergeantbulkin.cellular.ui.plans

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import by.sergeantbulkin.cellular.Operation
import by.sergeantbulkin.cellular.databinding.FragmentPlanBinding
import by.sergeantbulkin.cellular.room.AbonentsDatabase
import by.sergeantbulkin.cellular.room.model.PlanInfo
import by.sergeantbulkin.cellular.room.model.Service
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.gzeinnumer.mylibsearchviewdialog.dialog.searchViewDialogNew.SearchViewDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlin.math.cos

class PlanBottomSheet(val submitListener : (PlanInfo, Operation) -> Unit) : BottomSheetDialogFragment()
{
    //----------------------------------------------------------------------------------------------
    private lateinit var binding: FragmentPlanBinding
    //Выбранный тарифный план
    private var planInfo : PlanInfo? = null
    //Список услуг для отображения в диалоге
    private var services = listOf<Service>()
    //Список услуг выбранного плана
    private var chosenServices = arrayListOf<Service>()
    //ID выбранного плана
    private var planId = 0
    //Стоимость выбранного плана
    private var planCost = 0f
    //Адаптер
    private lateinit var planServiceAdapter: PlanServiceAdapter
    //CompositeDisposable для хранения подписок
    private val compositeDisposable = CompositeDisposable()
    //----------------------------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        arguments?.let {
            planInfo = it.getParcelable("planInfo")
        }
    }
    //----------------------------------------------------------------------------------------------
    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState: Bundle?) : View?
    {
        binding = FragmentPlanBinding.inflate(inflater, container, false)
        return binding.root
    }
    //----------------------------------------------------------------------------------------------
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        //Инициализация адаптера
        initAdapter()

        //Если не null, значит редактируем информацию
        if (planInfo != null)
        {
            fillData()
            //Изменить кнопки и Title
            binding.planButtonAdd.visibility = View.GONE
            binding.planButtonUpdate.visibility = View.VISIBLE
            binding.planButtonDelete.visibility = View.VISIBLE
            binding.planTitle.text = "Редактировать тариф"
        }

        //Загрузить необходимые данные
        loadData()

        //Настроить слушателей
        setUpViewListeners()

        //Повесить TextWatchers
        setTextWatchers()
    }
    //----------------------------------------------------------------------------------------------
    private fun initAdapter()
    {
        planServiceAdapter = PlanServiceAdapter { service ->
            //Удалить из списка выбранных услуг для тарифного плана
            chosenServices.remove(service)
            //Убрать из RecyclerView
            planServiceAdapter.removeItem(service)
            //Вычесть из стоимости тарифа стоимость удалённой услуги
            planCost -= service.cost
            binding.planCostTextInputEditText.setText("$planCost", TextView.BufferType.EDITABLE)
        }
        binding.planServicesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.planServicesRecyclerView.adapter = planServiceAdapter
    }
    //----------------------------------------------------------------------------------------------
    //Заполнить и запомнить данные выбранного плана
    private fun fillData()
    {
        //Запомнить
        planId = planInfo!!.planId
        chosenServices.addAll(planInfo!!.services)
        planCost = planInfo!!.planCost

        binding.planNameTextInputEditText.setText(planInfo!!.planName, TextView.BufferType.EDITABLE)
        binding.planCostTextInputEditText.setText("${planInfo!!.planCost}", TextView.BufferType.EDITABLE)
        planServiceAdapter.setServices(planInfo!!.services)
    }
    //----------------------------------------------------------------------------------------------
    //Загрузить все услуги
    private fun loadData()
    {
        //Загрузить услуги
        AbonentsDatabase.INSTANCE?.serviceDao()?.getServices()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                services = it
                binding.planFab.isEnabled = true
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
    //Настроить слушателей
    private fun setUpViewListeners()
    {
        //Обработка нажатия кнопки "Закрыть"
        binding.planArrowDown.setOnClickListener { dismiss() }

        //Обработка FAB
        binding.planFab.setOnClickListener {
            showMultiSelectDialog()
        }

        //Слушатели нажатия кнопок
        binding.planButtonAdd.setOnClickListener { if(checkInput()) sendResult(Operation.ADD) }
        binding.planButtonUpdate.setOnClickListener { if(checkInput()) sendResult(Operation.UPDATE) }
        binding.planButtonDelete.setOnClickListener{ submitListener(planInfo!!, Operation.DELETE); dismiss() }
    }
    //----------------------------------------------------------------------------------------------
    //Показать MultiSelectDialog для выбора услуг
    private fun showMultiSelectDialog()
    {
        val tempList = arrayListOf<Service>()
        tempList.addAll(services)
        tempList.removeAll(chosenServices)

        SearchViewDialog<Service>(parentFragmentManager)
            .setItems(tempList)
            .setTitle("Выберите")
            .setBtnOkTitle("Выбрать")
            .setBtnCancelTitle("Отмена")
            .onOkPressedCallBackMulti(SearchViewDialog.OnOkPressedMulti<Service>{
                data -> planServiceAdapter.addItems(data)
                chosenServices.addAll(data)
                binding.planCostTextInputEditText.setText(calculateCost(data), TextView.BufferType.EDITABLE)
            })
            .show()
    }
    //----------------------------------------------------------------------------------------------
    //Повесить TextWatchers
    private fun setTextWatchers()
    {
        //Название
        binding.planNameTextInputEditText.addTextChangedListener({ _, _, _, _ ->  },{ _, _, _, _ ->
            binding.planNameTextInputLayout.error = "" },{})
        //Стоимость
        binding.planCostTextInputEditText.addTextChangedListener({ _, _, _, _ ->  },{ _, _, _, _ ->
            binding.planCostTextInputLayout.error = "" },{})
    }
    //----------------------------------------------------------------------------------------------
    //Проверка введённых данных
    private fun checkInput() : Boolean
    {
        var flag = true

        //Название
        if (binding.planNameTextInputEditText.text.toString() == "")
        {
            binding.planNameTextInputLayout.error = "Введите название"
            flag = false
        }

        if (chosenServices.isNullOrEmpty())
        {
            binding.planCostTextInputLayout.error = "Выберите услуги"
            flag = false
        }

        return flag
    }
    //----------------------------------------------------------------------------------------------
    //Отправить результат в MainActivity
    private fun sendResult(operation: Operation)
    {
        val name = binding.planNameTextInputEditText.text.toString()

        planInfo = PlanInfo(planId, name, "", planCost)
        planInfo!!.services = services

        submitListener(planInfo!!, operation)
        dismiss()
    }
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    override fun onDestroy()
    {
        super.onDestroy()
        compositeDisposable.dispose()
    }
    //----------------------------------------------------------------------------------------------
    private fun calculateCost(data : List<Service>) : String
    {
        for (service in data)
        {
            planCost += service.cost
        }
        return "$planCost"
    }
    //----------------------------------------------------------------------------------------------
    companion object
    {
        @JvmStatic
        fun newInstance(planInfo: PlanInfo, submitListener : (PlanInfo, Operation) -> Unit) =
            PlanBottomSheet(submitListener).apply {
                arguments = Bundle().apply {
                    putParcelable("planInfo", planInfo)
                }
            }
    }
    //----------------------------------------------------------------------------------------------
}