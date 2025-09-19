package com.example.qrcode.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bld.qrscanner.R
import com.example.qrcode.MainViewModel
import com.example.qrcode.ViewPagerAdapter
import com.example.qrcode.data.AppDatabase
import com.example.qrcode.data.ScanHistory
import com.example.qrcode.data.ScanHistoryDao
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HistoryFragment : Fragment() {

    private lateinit var pager: ViewPager2 // creating object of ViewPager
    private lateinit var tab: TabLayout  // creating object of TabLayout
    private lateinit var deleteIcon: ImageView
    private lateinit var adapter: ViewPagerAdapter
    private lateinit var deletedItemList: MutableList<ScanHistory>

    private lateinit var mainViewModel: MainViewModel

    //    private val mainViewModel: MainViewModel by lazy {
//        ViewModelProvider(
//            requireActivity(),
//            MainViewModel.ScanHistoryViewModelFactory(activity?.application!!)
//        )[MainViewModel::class.java]
//    }
    private lateinit var dao: ScanHistoryDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        deletedItemList = mutableListOf()
        dao = context?.let { AppDatabase.getDatabase(it).ScanHistoryDao() }!!
        deleteIcon = view.findViewById(R.id.deleteIcon)
        pager = view.findViewById(R.id.historyViewPager)
        tab = view.findViewById(R.id.historyTabs)

        pager.offscreenPageLimit = 3
        adapter = ViewPagerAdapter(requireActivity())
//        adapter = activity?.let { ViewPagerAdapter(it) }!!

        pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

        })
//        // add fragment to the list
//        adapter.addFragment(TabScanFragment(), getString(R.string.scan))
//        adapter.addFragment(TabCreateFragment(), getString(R.string.create))
//        adapter.addFragment(TabFavouriteFragment(), getString(R.string.favourite))

        pager.adapter = adapter
        TabLayoutMediator(tab, pager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.scan)
                1 -> tab.text = getString(R.string.create)
                2 -> tab.text = getString(R.string.favourite)
            }
        }.attach()
//        tab.setupWithViewPager(pager)

        mainViewModel.listToDelete.observe(viewLifecycleOwner) {
            deletedItemList = it as MutableList<ScanHistory>
        }
        deleteIcon.setOnClickListener {
            if (deletedItemList.isNotEmpty()) {
                for (item in deletedItemList) {
                    dao.delete(item)
                }
                deletedItemList.clear()
                mainViewModel.setSelectedList(deletedItemList)
                mainViewModel.getAllRecords(dao)
                mainViewModel.listRecords.observe(viewLifecycleOwner) {
                }
            }
            Log.d("Delete List New", deletedItemList.size.toString())
        }
    }


}