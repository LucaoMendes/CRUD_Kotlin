package com.example.crudkotlin

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.lang.Exception

class SQLiteHelper(context:Context) :
    SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {

    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "crud.db"
        private const val TBL_USERS = "users"
        private const val ID = "id"
        private const val NAME = "name"
        private const val LOGIN = "login"
        private const val PASS = "pass"
    }


    override fun onCreate(db: SQLiteDatabase?) {
        val createTblUsers = ("CREATE TABLE " + TBL_USERS + "("
                    + ID + " INTEGER PRIMARY KEY,"
                    + NAME + " TEXT,"
                    + LOGIN + " TEXT,"
                    + PASS + " TEXT)")
        db?.execSQL(createTblUsers)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TBL_USERS")
        onCreate(db)
    }

    fun insertUser(user :UserModel):Long{
        val db = this.writableDatabase;
        val contentValues = ContentValues()
        contentValues.put(NAME, user.name)
        contentValues.put(LOGIN, user.login)
        contentValues.put(PASS, user.pass)

        val success = db.insert(TBL_USERS,null,contentValues)
        db.close()
        return success
    }
    @SuppressLint("Range")
    fun getUser(user: UserModel):UserModel{
        val db= this.writableDatabase
        val SQL = "SELECT * FROM $TBL_USERS WHERE $LOGIN = '${user.login}'"
        val cursor: Cursor?

        try{
            cursor = db.rawQuery(SQL,null)

        }catch (e: Exception){
            e.printStackTrace()
            db.execSQL(SQL)
            return UserModel(0,"0","0","0")
        }
        if(cursor.moveToFirst()){
            var id: Int = cursor.getInt(cursor.getColumnIndex(ID))
            var name:String = cursor.getString(cursor.getColumnIndex(NAME))
            var login:String = cursor.getString(cursor.getColumnIndex(LOGIN))
            var pass:String = cursor.getString(cursor.getColumnIndex(PASS))
            return UserModel(id = id, name = name, login = login, pass = pass)
        }

        return UserModel(0,"0","0","0")

    }

    @SuppressLint("Range")
    fun getAllUsers(): ArrayList<UserModel>{
        val usrList: ArrayList<UserModel> = ArrayList()
        val SQL = "SELECT * FROM $TBL_USERS"
        val db = this.writableDatabase

        val cursor: Cursor?

        try{
            cursor = db.rawQuery(SQL,null)

        }catch (e: Exception){
            e.printStackTrace()
            db.execSQL(SQL)
            return ArrayList()
        }

        var id: Int
        var name:String
        var login:String
        var pass:String

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex(ID))
                name = cursor.getString(cursor.getColumnIndex(NAME))
                login = cursor.getString(cursor.getColumnIndex(LOGIN))
                pass = cursor.getString(cursor.getColumnIndex(PASS))

                val usr = UserModel(id = id, name = name, login = login, pass = pass)

                usrList.add(usr)

            }while(cursor.moveToNext())
        }
        return usrList
    }

    fun removeUser(user :UserModel):Int{
        val db= this.writableDatabase
        val success = db.delete(TBL_USERS,"ID = ${user.id}",null)
        db.close()

        return success
    }



    fun updateUser(usr:UserModel):Int{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID,usr.id)
        contentValues.put(NAME,usr.name)
        contentValues.put(LOGIN,usr.login)
        contentValues.put(PASS,usr.pass)
        Log.e("ID",usr.id.toString())
        val success = db.update(TBL_USERS, contentValues, "ID = ${usr.id}", null)
        return success
    }
}