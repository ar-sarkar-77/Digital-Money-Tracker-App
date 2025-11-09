package com.anondo.dailymoneytrackerapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class DatabaseOpenHelper (context: Context?) : SQLiteOpenHelper(context , "Database_income_tracker" , null , 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table income (id INTEGER primary key autoincrement, reason TEXT , amount TEXT , time DOUBLE )")
        db?.execSQL("create table expense (id INTEGER primary key autoincrement, reason TEXT , amount TEXT , time DOUBLE )")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table if exists income")
        db?.execSQL("drop table if exists expense")
    }

    fun addIncome (amount: String , reason : String){

        var database : SQLiteDatabase = this.writableDatabase

        var conval = ContentValues()
        conval.put("amount" , ""+amount)
        conval.put("reason" , ""+reason)
        conval.put("time" , System.currentTimeMillis())

        database.insert("income" , null , conval)

    }

    //============================================================================================

    fun showIncome() : Cursor{

        var data : SQLiteDatabase = this.readableDatabase

        var cursor : Cursor = data.rawQuery("select * from income" , null)

        return cursor
    }
    //============================================================================================

    fun calculateIncome() : Double{
        var totalincome = 0.0
        var db : SQLiteDatabase = this.readableDatabase

        var cursor :Cursor = db.rawQuery("select * from income" , null)

        if (cursor!=null && cursor.count>0){
            while (cursor.moveToNext()){
                var income : String = cursor.getString(2)

                var incomes : Double = decryptData(""+income).toDouble()

                totalincome = totalincome+incomes
            }
        }
        return totalincome
    }

    //============================================================================================

    fun deleteIData(id : String){
        var dabs : SQLiteDatabase = this.writableDatabase
        dabs.execSQL("delete from income where id like "+id)
    }

    //============================================================================================

    fun addExpense (amount: String , reason : String){

        var databases : SQLiteDatabase = this.writableDatabase

        var conval = ContentValues()
        conval.put("amount" , ""+amount)
        conval.put("reason" , ""+reason)
        conval.put("time" , System.currentTimeMillis())

        databases.insert("expense" , null , conval)

    }

    //============================================================================================

    fun showExpense() : Cursor{

        var data : SQLiteDatabase = this.readableDatabase

        var cursor : Cursor = data.rawQuery("select * from expense" , null)

        return cursor
    }

    //============================================================================================

    fun calculateExpense() : Double{
        var totalexpense = 0.0
        var db : SQLiteDatabase = this.readableDatabase

        var cursor :Cursor = db.rawQuery("select * from expense" , null)

        if (cursor!=null && cursor.count>0){
            while (cursor.moveToNext()){
                var expense : String = cursor.getString(2)

                var expenses : Double = decryptData(""+expense).toDouble()

                totalexpense = totalexpense+expenses
            }
        }
        return totalexpense
    }

    //============================================================================================

    fun deleteEData(id : String){
        var dabs : SQLiteDatabase = this.writableDatabase
        dabs.execSQL("delete from expense where id like "+id)
    }

    //============================================================================================

    fun searchIInDataAll(key: String?): List<IncomeRow> {
        val db: SQLiteDatabase = this.readableDatabase
        val cursor: Cursor = db.rawQuery("select * from income", null)

        val results = mutableListOf<IncomeRow>()

        if (cursor != null && cursor.count > 0) {
            while (cursor.moveToNext()) {
                val id = cursor.getInt(0)
                val reasonEnc = cursor.getString(1)
                val amountEnc = cursor.getString(2)
                val time = cursor.getDouble(3)

                val reason = decryptData(reasonEnc)
                val amount = decryptData(amountEnc).toDouble()

                if (key.isNullOrEmpty() || reason.contains(key, ignoreCase = true)) {
                    results.add(IncomeRow(id, amount, reason, time))
                }
            }
        }

        cursor.close()
        return results
    }

    //============================================================================================

    fun searchEExDataAll(key: String?): List<ExpenseRow> {
        val db: SQLiteDatabase = this.readableDatabase
        val cursor: Cursor = db.rawQuery("select * from expense", null)
        val results = mutableListOf<ExpenseRow>()

        if (cursor != null && cursor.count > 0) {
            while (cursor.moveToNext()) {
                val id = cursor.getInt(0)
                val reasonEnc = cursor.getString(1)
                val amountEnc = cursor.getString(2)
                val time = cursor.getDouble(3)

                val reason = decryptData(reasonEnc)
                val amount = decryptData(amountEnc).toDouble()

                if (key.isNullOrEmpty() || reason.contains(key, ignoreCase = true)) {
                    results.add(ExpenseRow(id, amount, reason, time))
                }
            }
        }

        cursor.close()
        return results
    }

    //============================================================================================


    fun decryptData(text : String) : String{

        var decodeData = Base64.decode(text , Base64.DEFAULT)

        var password = "7fQ@Lp2!vC9#rXen"
        var passwordByte = password.toByteArray(Charsets.UTF_8)

        var secretKey = SecretKeySpec(passwordByte , "AES")

        var ciper : Cipher = Cipher.getInstance("AES")
        ciper.init(Cipher.DECRYPT_MODE , secretKey)
        var finalByte = ciper.doFinal(decodeData)

        return String(finalByte , Charsets.UTF_8)
    }

    //============================================================================================

}