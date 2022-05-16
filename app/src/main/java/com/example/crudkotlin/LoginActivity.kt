package com.example.crudkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LoginActivity : AppCompatActivity() {

    private lateinit var edLogin: EditText
    private lateinit var edPass: EditText

    private lateinit var btnLogin: Button
    private lateinit var btnCadastrar: Button

    private lateinit var database: SQLiteHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initView()
        database = SQLiteHelper(this)

        btnLogin.setOnClickListener{ authUser() }
    }

    fun initView(){
        edLogin = this.findViewById(R.id.txtUserLogin)
        edPass = this.findViewById(R.id.txtPassword)
        btnLogin = this.findViewById(R.id.btnLogin)
        btnCadastrar = this.findViewById(R.id.btnCad)
    }

    fun authUser(){
        val login = edLogin.text.toString().lowercase()
        val pass = edPass.text.toString()
        if(login.isEmpty() || pass.isEmpty())
            return Toast.makeText(this,"Você precisa preencher todos os campos!", Toast.LENGTH_SHORT).show()
        val usr = UserModel(login = login, pass = pass)

        val tempUser = database.getUser(usr)
        if(tempUser.login != usr.login)
            return Toast.makeText(this,"Usuário inválido!", Toast.LENGTH_SHORT).show()
        if(tempUser.login == usr.login && tempUser.pass != usr.pass)
            return Toast.makeText(this,"Senha Incorreta!", Toast.LENGTH_SHORT).show()

        Toast.makeText(this,"Logado com sucesso!", Toast.LENGTH_SHORT).show()
        val intent = Intent(this,MainActivity::class.java)
        return startActivity(intent)
    }
}