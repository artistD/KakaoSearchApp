package com.will_d.tp11kakaoplacesearchapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class SearchListFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: PlaceListRecyclerAdapter
    //이거 꼭 질문해야함

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_searchlist, container, false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainActivity :MainActivity = activity as MainActivity
        val place : ArrayList<Place>? = mainActivity.searchLocalApiResponse?.documents


        //리사이클러뷰는 그저 보여주는 친구고
        //실제로 만드는 친구는 누구라고????  아답터라구!!
        if (mainActivity.searchLocalApiResponse!=null){
            recyclerView = view.findViewById(R.id.recyclerview)
            adapter = PlaceListRecyclerAdapter(activity as Context, place as ArrayList<Place>)
            recyclerView.adapter=adapter
        }



    }
}