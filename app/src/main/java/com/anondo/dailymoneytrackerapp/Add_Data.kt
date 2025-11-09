package com.anondo.dailymoneytrackerapp

import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class Add_Data : AppCompatActivity() {

    companion object{
        var income = true
    }

    lateinit var btn_add_title : Button
    lateinit var btn_save : Button
    lateinit var et_reason : EditText
    lateinit var et_amount : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_data)
        btn_add_title = findViewById(R.id.btn_add_income_title)
        btn_save = findViewById(R.id.btn_save)
        et_reason = findViewById(R.id.et_reason)
        et_amount = findViewById(R.id.et_amount)
        var databaseHelper = DatabaseOpenHelper(this)


        if (income == true){
            btn_add_title.text = "Add Income"
        }
        else{
            btn_add_title.text = "Add Expense"
        }

        btn_save.setOnClickListener{

            var reason :String = et_reason.text.toString().trim()
            var amountS :String = et_amount.text.toString().trim()

            var reasons =  encryptData(reason)
            var amounts =  encryptData(amountS)

            if (et_amount.text.toString().isNotEmpty() && et_reason.text.toString().isNotEmpty()){

                if (income == true){
                    databaseHelper.addIncome(""+amounts ,""+ reasons)
                    Toast.makeText(this , "Data Inserted!" , Toast.LENGTH_SHORT).show()
                    finish()
                }
                else{
                    databaseHelper.addExpense(""+amounts ,""+ reasons)
                    Toast.makeText(this , "Data Inserted!" , Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            else if (et_amount.text.toString().isNotEmpty() && et_reason.text.toString().isEmpty()){

                if (income == true){
                    databaseHelper.addIncome(""+amounts ,""+ reasons)
                    Toast.makeText(this , "Data Inserted!" , Toast.LENGTH_SHORT).show()
                    finish()
                }
                else{
                    databaseHelper.addExpense(""+amounts , ""+reasons)
                    Toast.makeText(this , "Data Inserted!" , Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            else{
                Toast.makeText(this , "Input your Data!" , Toast.LENGTH_SHORT).show()

            }


        }


    }

    fun encryptData(text : String) : String{

        var textByte = text.toByteArray(Charsets.UTF_8)

        var password = "7fQ@Lp2!vC9#rXen"
        var passbyte = password.toByteArray(Charsets.UTF_8)

        var secretKye = SecretKeySpec(passbyte , "AES")

        var cipher : Cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE , secretKye)
        var encrypedTextData =  cipher.doFinal(textByte)

        return Base64.encodeToString(encrypedTextData , Base64.DEFAULT)

    }

    //=======================================================================
}