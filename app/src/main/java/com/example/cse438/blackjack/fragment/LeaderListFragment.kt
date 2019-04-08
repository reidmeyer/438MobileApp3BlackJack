package com.example.cse438.blackjack.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.app.Application
import android.content.Intent
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.util.DiffUtil
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.cse438.blackjack.R

import com.example.cse438.blackjack.viewmodel.LeaderViewModel
import kotlinx.android.synthetic.main.fragment_leader_list.*
import kotlinx.android.synthetic.main.leader_list_item.view.*
import java.util.ArrayList


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [LeaderListFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [LeaderListFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */

//leaderboard  list fragment
@SuppressLint("ValidFragment")
class LeaderListFragment() : Fragment() {


    private var adapter = LeaderListAdapter()
    private lateinit var viewModel: LeaderViewModel

    private var leaderList: ArrayList<String> = ArrayList()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_leader_list, container, false)
    }


    //onstart
    override fun onStart() {
        super.onStart()
        //creates grid view layout
        leader_list.layoutManager = LinearLayoutManager(this.context)
        leader_list.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        //intent and extra
        val extras = getActivity()!!.getIntent().getExtras()
        var parentextra = "wins"

        if (extras != null) {
            parentextra = extras.getString("someVariable")
        }

        viewModel = LeaderViewModel(Application(),  parentextra)

        //observe the view model
        val observer = Observer<ArrayList<String>> {
            leader_list.adapter = adapter
            //manage results
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun areItemsTheSame(p0: Int, p1: Int): Boolean {
                    if (p0>=leaderList.size || p1 >=leaderList.size)
                    {
                        return true
                    }
                    else
                    {
                        return leaderList[p0]==leaderList[p1]
                    }
                }

                //returns previous size
                override fun getOldListSize(): Int {
                    return leaderList.size
                }

                //returns current/new size
                override fun getNewListSize(): Int {
                    if (it == null) {
                        return 0
                    }
                    return it.size
                }

                //checks list contents
                override fun areContentsTheSame(p0: Int, p1: Int): Boolean {
                    if (p0>=leaderList.size || p1 >=leaderList.size)
                    {
                        return true
                    }
                    else
                    {
                        return leaderList[p0] == leaderList[p1]
                    }
                }
            })
            result.dispatchUpdatesTo(adapter)
            leaderList = it ?: ArrayList()
        }

        //listens to ans sorts view model
        viewModel.getLeaders(viewModel.sortby).observe(this, observer)

    }

    //adapter for listview
    inner class LeaderListAdapter: RecyclerView.Adapter<LeaderListAdapter.LeaderViewHolder>() {
        //creates the view adapter
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): LeaderViewHolder {
            val itemView = LayoutInflater.from(p0.context).inflate(R.layout.leader_list_item, p0, false)
            return LeaderViewHolder(itemView)
        }

        //sets the holder
        override fun onBindViewHolder(p0: LeaderViewHolder, p1: Int) {
            val product = leaderList[p1]

            p0.songTitle.text = product

        }

        //gets teh size
        override fun getItemCount(): Int {
            return leaderList.size
        }

        //assigns the parts to the ui
        inner class LeaderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            var songTitle: TextView = itemView.leader_list_text;

        }
    }



}
