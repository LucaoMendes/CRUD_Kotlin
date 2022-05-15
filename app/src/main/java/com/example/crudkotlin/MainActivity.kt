package com.example.crudkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var title: TextView
    private lateinit var edName: EditText
    private lateinit var edLogin: EditText
    private lateinit var edPass: EditText
    private lateinit var btnSave: Button
    private lateinit var btnView: Button
    private lateinit var btnClear: Button

    private lateinit var database: SQLiteHelper
    private lateinit var recyclerView: RecyclerView
    private var adapter: UserAdapter? = null
    private var usr: UserModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initRecyclerView()
        database = SQLiteHelper(this)

        btnSave.setOnClickListener{ addUser() }
        btnView.setOnClickListener{ getUsers() }
        btnClear.setOnClickListener{ clearEditText() }

        adapter?.setOnClickItem { updateUser(it) }
        adapter?.setOnClickDelete { removeUser(it) }
    }
    private fun getUsers(){
        val usrList = database.getAllUsers()
        Log.e("ppp","${usrList.size}")
        adapter?.addItems(usrList)
    }

    private fun removeUser(usr: UserModel){
        val success = database.removeUser(usr)
        if(success > 0){
            Toast.makeText(this,"Você removeu o usuário: ${usr.login}",Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this,"Erro ao remover: ${usr.login}",Toast.LENGTH_SHORT).show()
        }
        getUsers()
    }

    private fun updateUser(usr: UserModel){
        btnClear.isEnabled = true
        Toast.makeText(this,"Você selecionou o ${usr.name} ID: ${usr.id}",Toast.LENGTH_SHORT).show()
        title.setText("Atualizando ${usr.login}")
        edName.setText(usr.name)
        edLogin.setText(usr.login)
        edPass.setText(usr.pass)

        btnSave.setOnClickListener{
            val name = edName.text.toString()
            var login = edLogin.text.toString()
            val pass = edPass.text.toString()
            if(name.isEmpty() || login.isEmpty() || pass.isEmpty()){
                Toast.makeText(this,"Porfavor preencher todos os campos!",Toast.LENGTH_SHORT).show()
            }else if (name.length < 3){
                Toast.makeText(this,"Seu nome precisa ter no minimo 3 digitos!!",Toast.LENGTH_SHORT).show()
            }else if (login.length < 5){
                Toast.makeText(this,"Seu login precisa ter no minimo 5 digitos!!",Toast.LENGTH_SHORT).show()
            }else if (pass.length < 5){
                Toast.makeText(this,"Sua senha precisa ter no minimo 5 digitos!!",Toast.LENGTH_SHORT).show()
            }else {
                login = login.lowercase()
                val user = UserModel(id= usr.id,name = name, login = login, pass = pass)
                val status = database.updateUser(user)
                Log.e("STATUS UPDATE",status.toString())
                if(status > 0){
                    Toast.makeText(this,"Perfeito!",Toast.LENGTH_SHORT).show()
                    clearEditText()
                    getUsers()

                }else{
                    Toast.makeText(this,"Erro ao atualizar usuário!",Toast.LENGTH_SHORT).show()
                }


            }

        }
    }



    private fun addUser() {

        val name = edName.text.toString()
        var login = edLogin.text.toString()
        val pass = edPass.text.toString()
        if(name.isEmpty() || login.isEmpty() || pass.isEmpty()){
            Toast.makeText(this,"Porfavor preencher todos os campos!",Toast.LENGTH_SHORT).show()
        }else if (name.length < 3){
            Toast.makeText(this,"Seu nome precisa ter no minimo 3 digitos!!",Toast.LENGTH_SHORT).show()
        }else if (login.length < 5){
            Toast.makeText(this,"Seu login precisa ter no minimo 5 digitos!!",Toast.LENGTH_SHORT).show()
        }else if (pass.length < 5){
            Toast.makeText(this,"Sua senha precisa ter no minimo 5 digitos!!",Toast.LENGTH_SHORT).show()
        }else {
            login = login.lowercase()
            val user = UserModel(name = name, login = login, pass = pass)
            val status = database.insertUser(user)

            if(status > -1){
                Toast.makeText(this,"Perfeito!",Toast.LENGTH_SHORT).show()
                clearEditText()
                getUsers()
            }else{
                Toast.makeText(this,"Erro ao inserir usuário!",Toast.LENGTH_SHORT).show()
            }


        }

    }

    private fun clearEditText(){
        edName.setText("")
        edLogin.setText("")
        edPass.setText("")
        title.setText(R.string.the_title)
        btnClear.isEnabled = false
        edName.requestFocus()
        btnSave.setOnClickListener{ addUser() }
    }

    private fun initRecyclerView(){
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = UserAdapter()
        recyclerView.adapter = adapter

    }
    private fun initView(){
        title = findViewById(R.id.txtTitle)
        edName = findViewById(R.id.txtName)
        edLogin = findViewById(R.id.txtLogin)
        edPass = findViewById(R.id.txtUserPass)
        btnSave = findViewById(R.id.btnSave)
        btnView = findViewById(R.id.btnView)
        btnClear = findViewById(R.id.btnClear)
        recyclerView = findViewById(R.id.listUsers)
    }

    override fun onStart() {
        super.onStart()
        getUsers()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}