package app.kobuggi.hyuabot.ui.subway

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import app.kobuggi.hyuabot.databinding.FragmentSubwayBinding
import app.kobuggi.hyuabot.ui.subway.SubwayArrivalListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubwayFragment : Fragment() {
    private val vm by viewModels<SubwayViewModel>()
    private lateinit var binding: FragmentSubwayBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSubwayBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.vm = vm

        val subwayArrivalListAdapter = SubwayArrivalListAdapter(requireContext(), listOf())
        binding.subwayArrivalList.adapter = subwayArrivalListAdapter
        binding.subwayArrivalList.layoutManager = LinearLayoutManager(requireContext())
        if(binding.subwayArrivalList.itemAnimator is SimpleItemAnimator){
            (binding.subwayArrivalList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
        vm.subwayData.observe(viewLifecycleOwner) {
            subwayArrivalListAdapter.setSubwayData(it)
            vm.isLoading.value = false
        }
        binding.refreshLayout.setOnRefreshListener {
            vm.stopFetchData()
            vm.startFetchData()
            binding.refreshLayout.isRefreshing = false
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        vm.startFetchData()
    }

    override fun onPause() {
        super.onPause()
        vm.stopFetchData()
    }
}