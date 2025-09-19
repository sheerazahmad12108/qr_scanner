package com.example.qrcode.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bld.qrscanner.R
import com.example.qrcode.TabFavouriteListAdapter
import com.example.qrcode.activities.DetailsActivity
import com.example.qrcode.data.AppDatabase
import com.example.qrcode.data.ScanHistory
import com.example.qrcode.data.ScanHistoryDao
import java.util.Locale

class TabFavouriteFragment : Fragment() {

    private lateinit var tabFavouriteListAdapter: TabFavouriteListAdapter
    private lateinit var listTabScan: MutableList<ScanHistory>
    private lateinit var rvFavouriteHistory: RecyclerView
    private lateinit var tabFavouriteLayout: LinearLayout
    private lateinit var noDataLayout: LinearLayout
    private lateinit var edtSearch: EditText
    private lateinit var searchBar: RelativeLayout
    private lateinit var dao: ScanHistoryDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tab_favourite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dao = context?.let { AppDatabase.getDatabase(it).ScanHistoryDao() }!!
        listTabScan = mutableListOf()
        rvFavouriteHistory = view.findViewById(R.id.rvFavouriteHistory)
        tabFavouriteLayout = view.findViewById(R.id.tabFavouriteLayout)
        noDataLayout = view.findViewById(R.id.noDataLayout)
        edtSearch = view.findViewById(R.id.edtSearch)
        searchBar = view.findViewById(R.id.searchBar)
//        setAdapter()
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
            tabFavouriteListAdapter.filterList(filterList)
        } else {
            tabFavouriteListAdapter.filterList(filterList)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapter() {
        listTabScan = dao.getFavourite() as MutableList<ScanHistory>

        if (listTabScan.isNotEmpty()) {
            tabFavouriteLayout.visibility = View.VISIBLE
            searchBar.visibility = View.VISIBLE
            noDataLayout.visibility = View.GONE
            tabFavouriteListAdapter = TabFavouriteListAdapter(listTabScan)
            rvFavouriteHistory.layoutManager = LinearLayoutManager(context)
            rvFavouriteHistory.adapter = tabFavouriteListAdapter
            tabFavouriteListAdapter.notifyDataSetChanged()

            tabFavouriteListAdapter.setOnItemClick(object : TabFavouriteListAdapter.OnItemClick{
                override fun onItemClick(position: Int, model: ScanHistory) {
                    val bundle = Bundle()
                    bundle.putInt("id", model.id!!)
                    val intent = Intent(activity, DetailsActivity::class.java)
                    intent.putExtras(bundle)
                    activity!!.startActivity(intent)
                }
            })
        } else {
            tabFavouriteLayout.visibility = View.GONE
            searchBar.visibility = View.GONE
//            edtSearch.isEnabled = false
            noDataLayout.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        setAdapter()
        edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (listTabScan.isNotEmpty()) {
                    filterList(p0.toString())
                }
            }

        })
    }
}