package com.example.qrcode.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bld.qrscanner.R
import com.example.qrcode.MainViewModel
import com.example.qrcode.TabScanListAdapter
import com.example.qrcode.activities.DetailsActivity
import com.example.qrcode.data.AppDatabase
import com.example.qrcode.data.ScanHistory
import com.example.qrcode.data.ScanHistoryDao
import java.util.Locale

class TabScanFragment : Fragment() {

    private lateinit var tabScanListAdapter: TabScanListAdapter
    private lateinit var listTabScan: MutableList<ScanHistory>
    private lateinit var listDeleteTabScan: MutableList<ScanHistory>
    private lateinit var rvTabScab: RecyclerView
    private lateinit var edtSearch: EditText
    private lateinit var noDataLayout: LinearLayout
    private lateinit var tabScanLayout: LinearLayout
    private lateinit var searchBar: RelativeLayout
    private lateinit var dao: ScanHistoryDao
    private lateinit var mainViewModel: MainViewModel
//    private val mainViewModel: MainViewModel by lazy {
//        ViewModelProvider(
//            this,
//            MainViewModel.ScanHistoryViewModelFactory(activity?.application!!)
//        )[MainViewModel::class.java]
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tab_scan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        listTabScan = mutableListOf()
        listDeleteTabScan = mutableListOf()
        rvTabScab = view.findViewById(R.id.rvScanHistory)
        noDataLayout = view.findViewById(R.id.noDataLayout)
        tabScanLayout = view.findViewById(R.id.tabScanLayout)
        edtSearch = view.findViewById(R.id.edtSearch)
        searchBar = view.findViewById(R.id.searchBar)
        dao = context?.let { AppDatabase.getDatabase(it).ScanHistoryDao() }!!
        listTabScan = dao.getScanned() as MutableList<ScanHistory>
//        tabScanListAdapter = TabScanListAdapter(listTabScan)
//        rvTabScab.layoutManager = LinearLayoutManager(context)
//        rvTabScab.adapter = tabScanListAdapter
//
//        if (listTabScan.isNotEmpty()) {
//            noDataLayout.visibility = View.GONE
//            tabScanLayout.visibility = View.VISIBLE
//            searchBar.visibility = View.VISIBLE
//        } else {
//            noDataLayout.visibility = View.VISIBLE
//            tabScanLayout.visibility = View.GONE
//            searchBar.visibility = View.GONE
//        }
//        mainViewModel.listRecords.observe(viewLifecycleOwner, Observer {
//            listTabScan = it as MutableList<ScanHistory>
//            tabScanListAdapter.setRecords(it)
//        })


        setAdapter()
        edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                filterList(p0.toString())
            }

        })

        mainViewModel.listRecords.observe(viewLifecycleOwner) {
            tabScanListAdapter.setRecords(it)
            if (it.isEmpty()) {
                noDataLayout.visibility = View.VISIBLE
                tabScanLayout.visibility = View.GONE
                searchBar.visibility = View.GONE
            }
//            if (it.isNotEmpty()) {
//                noDataLayout.visibility = View.GONE
//                tabScanLayout.visibility = View.VISIBLE
//                searchBar.visibility = View.VISIBLE
//            } else {
//                noDataLayout.visibility = View.VISIBLE
//                tabScanLayout.visibility = View.GONE
//                searchBar.visibility = View.GONE
//            }
        }
    }

    private fun filterList(filterText: String) {
        val filterList: MutableList<ScanHistory> = mutableListOf()
        for (item in listTabScan) {
            if (item.link?.toLowerCase(Locale.ROOT)
                    ?.contains(filterText.toLowerCase(Locale.ROOT)) == true
            ) {
                filterList.add(item)
            }
        }

        if (filterList.isEmpty()) {
            Toast.makeText(context, "No Data Found", Toast.LENGTH_LONG).show()
            tabScanListAdapter.filterList(filterList)
        } else {
            tabScanListAdapter.filterList(filterList)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapter() {
//        listTabScan = dao.getAll() as MutableList<ScanHistory>
//        mainViewModel.getAllRecords(dao)
//        mainViewModel.listRecords.observe(viewLifecycleOwner, Observer {
//            listTabScan = it as MutableList<ScanHistory>
//            if (listTabScan.isNotEmpty()) {
//                noDataLayout.visibility = View.GONE
//                tabScanLayout.visibility = View.VISIBLE
//                searchBar.visibility = View.VISIBLE
//            } else {
//                noDataLayout.visibility = View.VISIBLE
//                tabScanLayout.visibility = View.GONE
//                searchBar.visibility = View.GONE
//            }
//        })
        tabScanListAdapter = TabScanListAdapter(listTabScan)
        rvTabScab.layoutManager = LinearLayoutManager(context)
        rvTabScab.adapter = tabScanListAdapter

        tabScanListAdapter.setOnClickListener(object : TabScanListAdapter.OnClickListener {
            override fun onClick(position: Int, model: ScanHistory) {
                val bundle = Bundle()
                bundle.putInt("id", model.id!!)
                val intent = Intent(activity, DetailsActivity::class.java)
                intent.putExtras(bundle)
                activity!!.startActivity(intent)

//                bundle.putString("text", model.link)
//                bundle.putString("time", model.date)
//                model.favourite?.let { bundle.putBoolean("favourite", it) }
//                bundle.putSerializable("format", model.format)
//                findNavController().navigate(
//                    R.id.action_navigation_history_to_detailsFragment,
//                    bundle
//                )
            }

            override fun onClickFavourite(position: Int, model: ScanHistory) {
                model.favourite = !model.favourite!!
                dao.update(model)
                tabScanListAdapter.notifyItemChanged(position)
            }

        })

        tabScanListAdapter.setonCheckChangeListener(object :
            TabScanListAdapter.OnCheckChangeListener {
            override fun onCheckChanged(position: Int, model: ScanHistory, isChecked: Boolean) {
//                itemPosition = position
                if (isChecked) {
                    listDeleteTabScan.add(model)
                    mainViewModel.setSelectedList(listDeleteTabScan)
                }

            }
        })
    }

    override fun onResume() {
        super.onResume()
//        listTabScan = dao.getAll() as MutableList<ScanHistory>
        mainViewModel.getAllRecords(dao)
        if (listTabScan.isNotEmpty()) {
            noDataLayout.visibility = View.GONE
            tabScanLayout.visibility = View.VISIBLE
            searchBar.visibility = View.VISIBLE
        } else {
            noDataLayout.visibility = View.VISIBLE
            tabScanLayout.visibility = View.GONE
            searchBar.visibility = View.GONE
        }
//        setAdapter()
    }

}