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
import com.example.qrcode.TabCreateListAdapter
import com.example.qrcode.activities.DetailsActivity
import com.example.qrcode.data.AppDatabase
import com.example.qrcode.data.ScanHistory
import com.example.qrcode.data.ScanHistoryDao
import java.util.Locale


class TabCreateFragment : Fragment() {

    private lateinit var tabCreateListAdapter: TabCreateListAdapter
    private lateinit var listTabCreate: MutableList<ScanHistory>
    private lateinit var listDeleteTabCreate: MutableList<ScanHistory>
    private lateinit var rvTabCreate: RecyclerView
    private lateinit var edtSearch: EditText
    private lateinit var noDataLayout: LinearLayout
    private lateinit var tabCreateLayout: LinearLayout
    private lateinit var searchBar: RelativeLayout
    private lateinit var dao: ScanHistoryDao
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tab_create, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        listTabCreate = mutableListOf()
        listDeleteTabCreate = mutableListOf()
        rvTabCreate = view.findViewById(R.id.rvCreateHistory)
        noDataLayout = view.findViewById(R.id.noDataLayout)
        tabCreateLayout = view.findViewById(R.id.tabCreateLayout)
        edtSearch = view.findViewById(R.id.edtSearch)
        searchBar = view.findViewById(R.id.searchBar)
        dao = context?.let { AppDatabase.getDatabase(it).ScanHistoryDao() }!!
        listTabCreate = dao.getCreated() as MutableList<ScanHistory>

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

        mainViewModel.listCreatedRecords.observe(viewLifecycleOwner) {
            tabCreateListAdapter.setRecords(it)
            if (it.isEmpty()) {
                noDataLayout.visibility = View.VISIBLE
                tabCreateLayout.visibility = View.GONE
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
        for (item in listTabCreate) {
            if (item.link?.toLowerCase(Locale.ROOT)
                    ?.contains(filterText.toLowerCase(Locale.ROOT)) == true
            ) {
                filterList.add(item)
            }
        }

        if (filterList.isEmpty()) {
            Toast.makeText(context, "No Data Found", Toast.LENGTH_LONG).show()
            tabCreateListAdapter.filterList(filterList)
        } else {
            tabCreateListAdapter.filterList(filterList)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapter() {
        tabCreateListAdapter = TabCreateListAdapter(listTabCreate)
        rvTabCreate.layoutManager = LinearLayoutManager(context)
        rvTabCreate.adapter = tabCreateListAdapter

        tabCreateListAdapter.setOnClickListener(object : TabCreateListAdapter.OnClickListener {
            override fun onClick(position: Int, model: ScanHistory) {
                val bundle = Bundle()
                bundle.putInt("id", model.id!!)
                val intent = Intent(activity, DetailsActivity::class.java)
                intent.putExtras(bundle)
                activity!!.startActivity(intent)
            }

            override fun onClickFavourite(position: Int, model: ScanHistory) {
                model.favourite = !model.favourite!!
                dao.update(model)
                tabCreateListAdapter.notifyItemChanged(position)
            }

        })

        tabCreateListAdapter.setonCheckChangeListener(object :
            TabCreateListAdapter.OnCheckChangeListener {
            override fun onCheckChanged(position: Int, model: ScanHistory, isChecked: Boolean) {
                if (isChecked) {
                    listDeleteTabCreate.add(model)
                    mainViewModel.setSelectedList(listDeleteTabCreate)
                }

            }
        })
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.getAllCreatedRecords(dao)
        if (listTabCreate.isNotEmpty()) {
            noDataLayout.visibility = View.GONE
            tabCreateLayout.visibility = View.VISIBLE
            searchBar.visibility = View.VISIBLE
        } else {
            noDataLayout.visibility = View.VISIBLE
            tabCreateLayout.visibility = View.GONE
            searchBar.visibility = View.GONE
        }
    }

}