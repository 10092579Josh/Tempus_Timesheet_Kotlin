package com.example.tempus_project.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tempus_project.R


import com.example.tempus_project.model.Category

class CategoryAdapter(val c:Context, val catList: ArrayList<Category>): RecyclerView.Adapter<CategoryAdapter.UserViewHolder>()
{

    inner class UserViewHolder(val v: View): RecyclerView.ViewHolder(v){
        val cName = v.findViewById<TextView>(R.id.mTitle2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return catList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

    }

}