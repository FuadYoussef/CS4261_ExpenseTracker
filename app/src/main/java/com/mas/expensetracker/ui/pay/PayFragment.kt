//Sampled from https://github.com/LucasPM97/GPaySample

package com.mas.expensetracker.ui.pay

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.mas.expensetracker.R
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.fragment_pay.*


class PayFragment : Fragment() {

    private lateinit var viewModel: PayViewModel
    private var mountToPay: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pay, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PayViewModel::class.java)


        setObservable()
        //var custom = 0
        optionList.setOnCheckedChangeListener{ group, checkedId ->
//                while(checkedId==price4.id && specific_amount?.text.isNullOrEmpty()){
//
//                }
//                custom = specific_amount?.text.toString().toInt()
                val newPrice =  when(checkedId){
                    price1.id-> 1
                    price2.id-> 5
                    price3.id-> 10
                    price4.id-> specific_amount?.text.toString().toInt()
                    else -> 0
                }

            viewModel.setMountToPay(newPrice)
            confirm_button.isEnabled = true
        }

        confirm_button.setOnClickListener {

            val action = PayFragmentDirections.actionPayFragmentToCompletePayFragment(mountToPay);

            findNavController().navigate(action)
        }
    }

    private fun setObservable(){
        val price:MutableLiveData<Int?>? = viewModel.getMountToPay()

        price?.observe(viewLifecycleOwner, Observer {
                it?.let {
                    priceMount_text.text = "$ ${it.toString()}"
                    mountToPay = it
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(
            true // default to enabled
        ) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,  // LifecycleOwner
            callback
        )
    }
}
