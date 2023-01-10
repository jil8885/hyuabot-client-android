package app.kobuggi.hyuabot.ui.bus.realtime

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import app.kobuggi.hyuabot.component.card.bus.RealtimeRouteCardAdapter
import app.kobuggi.hyuabot.databinding.FragmentBusRealtimeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RealtimeFragment : Fragment() {
    companion object {
        fun newInstance() = RealtimeFragment()
    }
    private val viewModel: RealtimeViewModel by viewModels()
    private val binding by lazy { FragmentBusRealtimeBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val adapter = RealtimeRouteCardAdapter(
            requireContext(),
            0,
            { index -> viewModel.setBookmark(index) },
            { routeID, routeName, startStop -> viewModel.openTimetable(routeID, routeName, startStop) }
        )
        viewModel.fetchData()
        viewModel.getBookmark()
        viewModel.start()
        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.busArrivalProgress.visibility = if (it) View.VISIBLE else View.GONE
        }
        viewModel.bookmarkIndex.observe(viewLifecycleOwner) {
            adapter.updateBookmarkIndex(it)
            binding.busRealtimeRecyclerView.scrollToPosition(it)
        }
        viewModel.conventionCenterBusArrivalList.observe(viewLifecycleOwner) {
            adapter.updateConventionCenterData(it)
        }
        viewModel.mainGateBusArrivalList.observe(viewLifecycleOwner) {
            adapter.updateMainGateData(it)
        }
        viewModel.sangnoksuArrivalList.observe(viewLifecycleOwner) {
            adapter.updateSangnoksuData(it)
        }
        viewModel.seonganHighSchoolArrivalList.observe(viewLifecycleOwner) {
            adapter.updateSeonganHighSchoolData(it)
        }
        viewModel.timetableEvent.observe(viewLifecycleOwner) {
            if (it.routeID > 0) {
                val action = RealtimeFragmentDirections.openBusTimetable(it.routeID, it.routeName, it.stopID)
                viewModel.openTimetable(-1, "", -1)
                findNavController().navigate(action)
            }
        }
        binding.busRealtimeRecyclerView.adapter = adapter
        binding.busRealtimeRecyclerView.itemAnimator = null
        binding.refreshLayout.setOnRefreshListener {
            viewModel.fetchData()
            binding.refreshLayout.isRefreshing = false
        }
        return binding.root
    }
}