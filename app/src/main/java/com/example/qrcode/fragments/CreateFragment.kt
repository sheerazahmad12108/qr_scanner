package com.example.qrcode.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bld.qrscanner.R
import com.example.qrcode.CreateFragmentListAdapter
import com.example.qrcode.CreateList

class CreateFragment : Fragment() {

    private lateinit var rvCreateItems: RecyclerView
    private lateinit var createFragmentListAdapter: CreateFragmentListAdapter
    private lateinit var createItemList: MutableList<CreateList>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.setStatusBarColor(Color.TRANSPARENT)
        createItemList = mutableListOf()
        rvCreateItems = view.findViewById(R.id.rvCreateItems)
        createFragmentListAdapter = CreateFragmentListAdapter(createItemList)
        rvCreateItems.layoutManager = GridLayoutManager(context, 2)
        rvCreateItems.adapter = createFragmentListAdapter
        setList()

        createFragmentListAdapter.setOnItemClick(object : CreateFragmentListAdapter.OnItemClick{
            override fun onItemClick(position: Int, model: CreateList) {

                val bundle = Bundle()
                bundle.putString("title",model.name)
                bundle.putInt("position",position)
                findNavController().navigate(R.id.action_navigation_create_to_generateQRCodeFragment,bundle)
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setList() {
        createItemList.add(CreateList(getString(R.string.website), R.drawable.website_icon))
        createItemList.add(CreateList(getString(R.string.clipboard), R.drawable.clip_board_icon))
        createItemList.add(CreateList(getString(R.string.wifi), R.drawable.wi_fi_icon))
        createItemList.add(CreateList(getString(R.string.facebook), R.drawable.face_book_icon))
        createItemList.add(CreateList(getString(R.string.youtube), R.drawable.you_tube_icon))
        createItemList.add(CreateList(getString(R.string.whatsapp), R.drawable.whats_app_icon))
        createItemList.add(CreateList(getString(R.string.text), R.drawable.text_icon))
        createItemList.add(CreateList(getString(R.string.contacts), R.drawable.contacts_icon))
        createItemList.add(CreateList(getString(R.string.telephone), R.drawable.telephone_icon))
        createItemList.add(CreateList(getString(R.string.email), R.drawable.email_icon))
        createItemList.add(CreateList(getString(R.string.sms), R.drawable.sms_icon))
        createItemList.add(CreateList(getString(R.string.my_card), R.drawable.my_card_icon))
        createItemList.add(CreateList(getString(R.string.paypal), R.drawable.paypal_icon))
        createItemList.add(CreateList(getString(R.string.instagram), R.drawable.insta_icon))
        createItemList.add(CreateList(getString(R.string.viber), R.drawable.viber_icon))
        createItemList.add(CreateList(getString(R.string.twitter), R.drawable.twitter_icon))
        createItemList.add(CreateList(getString(R.string.calender), R.drawable.calender_icon))
        createItemList.add(CreateList(getString(R.string.spotify), R.drawable.spotify_icon))
        createFragmentListAdapter.notifyDataSetChanged()
    }



}