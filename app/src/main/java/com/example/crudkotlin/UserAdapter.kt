package com.example.crudkotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var usrList: ArrayList<UserModel> = ArrayList()
    private var onClickItem:((UserModel) -> Unit)? = null
    private var onClickDelete: ((UserModel) -> Unit)? = null

    fun addItems(items:ArrayList<UserModel>){
        this.usrList = items
        notifyDataSetChanged()
    }

    fun setOnClickItem(callback: (UserModel)->Unit){
        this.onClickItem = callback
    }
    fun setOnClickDelete(callback: (UserModel)->Unit){
        this.onClickDelete = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = UserViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.card_items_users,parent,false)
    )

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val usr = usrList[position]
        holder.bindView(usr)
        holder.itemView.setOnClickListener{ onClickItem?.invoke(usr) }
        holder.btnDelete.setOnClickListener{ onClickDelete?.invoke(usr) }
    }

    override fun getItemCount(): Int {
       return this.usrList.size
    }

    class UserViewHolder(var view: View): RecyclerView.ViewHolder(view){

        private var id = view.findViewById<TextView>(R.id.cardID)
        private var name = view.findViewById<TextView>(R.id.cardUserName)
        private var login = view.findViewById<TextView>(R.id.cardUserLogin)
        var btnDelete = view.findViewById<TextView>(R.id.cardBtnDel)
        private lateinit var database: SQLiteHelper

        fun bindView(usr:UserModel){
            id.text = usr.id.toString()
            name.text = usr.name
            login.text = usr.login
        }
    }
}