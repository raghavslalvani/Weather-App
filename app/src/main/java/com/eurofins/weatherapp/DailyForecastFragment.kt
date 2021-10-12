package com.eurofins.weatherapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.eurofins.weatherapp.adapter.WeatherForecastAdapter
import com.eurofins.weatherapp.data.WeatherViewModel
import com.eurofins.weatherapp.databinding.FragmentDailyForecastBinding

class DailyForecastFragment : Fragment() {

    private val viewModel: WeatherViewModel by activityViewModels()
    private lateinit var _binding: FragmentDailyForecastBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDailyForecastBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.adapter = WeatherForecastAdapter(viewModel.dataset)
    }

}