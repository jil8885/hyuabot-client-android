package app.kobuggi.hyuabot.ui.contact

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import app.kobuggi.hyuabot.R
import app.kobuggi.hyuabot.databinding.FragmentContactBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactFragment : Fragment() {
    private val vm by viewModels<ContactViewModel>()
    private lateinit var binding: FragmentContactBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = vm
        val tabAdapter = ContactTabAdapter(this)
        binding.contactViewpager.adapter = tabAdapter
        TabLayoutMediator(binding.contactTab, binding.contactViewpager) { tab, position ->
            tab.text = context?.getString(
                when (position) {
                    0 -> R.string.in_school
                    1 -> R.string.out_school
                    else -> R.string.out_school
                }
            )
        }.attach()

        binding.searchInput.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    vm.queryString.value = query
                }
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                Log.d("query", binding.contactViewpager.currentItem.toString())
                if (query != null) {
                    vm.queryString.value = query
                }
                return true
            }
        })

        val toast = Toast.makeText(context,  requireContext().getString(R.string.contact_message), Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
        return binding.root
    }
}