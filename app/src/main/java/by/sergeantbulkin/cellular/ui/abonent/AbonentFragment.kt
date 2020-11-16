package by.sergeantbulkin.cellular.ui.abonent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.sergeantbulkin.cellular.databinding.FragmentAbonentBinding
import by.sergeantbulkin.cellular.room.model.Abonent

class AbonentFragment : Fragment()
{
    // TODO: Rename and change types of parameters
    private var abonent: Abonent? = null

    //Binding
    private lateinit var binding: FragmentAbonentBinding

    private lateinit var viewModel: AbonentViewModel

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        arguments?.let {
            abonent = it.getParcelable("abonent")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        //Инициализация ViewModel
        viewModel = ViewModelProvider(this).get(AbonentViewModel::class.java)
        //Инициализация переменной биднинга
        binding = FragmentAbonentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
    }

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
}