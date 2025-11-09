package com.anondo.dailymoneytrackerapp

import android.content.Context
import android.database.Cursor
import android.media.RouteListingPreference.Item
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anondo.dailymoneytrackerapp.Show_Data.Companion.dbsa
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class Show_Data : AppCompatActivity() {

    companion object{
        var income = true
        lateinit var dbsa :DatabaseOpenHelper
    }

    lateinit var recyclerView :RecyclerView
    lateinit var showDataType :TextView
    lateinit var searchData :EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_data)
        recyclerView = findViewById(R.id.recyclerView)
        showDataType = findViewById(R.id.showDataType)
        searchData = findViewById(R.id.searchData)

        dbsa = DatabaseOpenHelper(this)

        if (income==true){
            LoadData(dbsa.showIncome())
        }else if (income==false){
            LoadData(dbsa.showExpense())
        }

        searchData.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val query = searchData.text.toString().trim()

                if (income) {
                    val results = dbsa.searchIInDataAll(query)
                    val dataListIn = ArrayList<DataClass>()

                    for (item in results) {
                        val dataItem = DataClass(item.id, item.amount, item.reason, item.time)
                        dataListIn.add(dataItem)
                    }

                    recyclerView.layoutManager = LinearLayoutManager(this@Show_Data)
                    recyclerView.adapter = MyAdapter(this@Show_Data, dataListIn) {
                        LoadData(dbsa.showIncome())
                    }

                } else {
                    val results = dbsa.searchEExDataAll(query)
                    val dataListEx = ArrayList<DataClassExpense>()

                    for (item in results) {
                        val dataItem = DataClassExpense(item.id, item.amount, item.reason, item.time)
                        dataListEx.add(dataItem)
                    }

                    recyclerView.layoutManager = LinearLayoutManager(this@Show_Data)
                    recyclerView.adapter = MyAdapterExpense(this@Show_Data, dataListEx) {
                        LoadData(dbsa.showExpense())
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

    }

    //============================================================================================

    fun LoadData(cursor : Cursor) {

        var arrayList = ArrayList<DataClass>()
        var arrayListE = ArrayList<DataClassExpense>()

        if (income == true) {

            showDataType.text = "Income Data"
          //  var cursor: Cursor = dbsa.showIncome()

            if (cursor != null && cursor.count > 0) {

                while (cursor.moveToNext()) {
                    var id: Int = cursor.getInt(0)
                    var amountS: String = cursor.getString(2)
                    var reasonS: String = cursor.getString(1)
                    var time: Double = cursor.getDouble(3)

                    var amountD = decryptData(amountS)
                    var reason = decryptData(reasonS)

                    var amount : Double = amountD.toDouble()

                    arrayList.add(
                        DataClass(id, amount, "" + reason, time)
                    )

                }

                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.adapter = MyAdapter(this, arrayList){
                    LoadData(dbsa.showIncome())
                }


            } else {
              //  Toast.makeText(applicationContext, "No Data", Toast.LENGTH_SHORT).show()
            }

        } else if (income==false) {

            showDataType.text = "Expense Data"
         //   var cursor: Cursor = dbsa.showExpense()

            if (cursor != null && cursor.count > 0) {

                while (cursor.moveToNext()) {
                    var id: Int = cursor.getInt(0)
                    var amountS: String = cursor.getString(2)
                    var reasonS: String = cursor.getString(1)
                    var time: Double = cursor.getDouble(3)

                    var amountD = decryptData(amountS)
                    var reason = decryptData(reasonS)

                    var amount : Double = amountD.toDouble()

                    arrayListE.add(
                        DataClassExpense(id, amount, "" + reason, time)
                    )

                }

                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.adapter = MyAdapterExpense(this, arrayListE){
                    LoadData(dbsa.showExpense())
                }


            } else {
             //   Toast.makeText(applicationContext, "No Data", Toast.LENGTH_SHORT).show()
            }

            }

        }

    //============================================================================================

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

    //=================================================================================

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

    //=========================================================================================================================
    //=========================================================================================================================

    class MyAdapter(var context: Context , var datalistI : ArrayList<DataClass> , private val onDeleteCallBack : () -> Unit) : RecyclerView.Adapter<MyAdapter.VIewHolder>(){

        class VIewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
            var titleT : TextView = itemView.findViewById(R.id.titleT)
            var reasonT : TextView = itemView.findViewById(R.id.reasonT)
            var amountT : TextView = itemView.findViewById(R.id.amountT)
            var timeT : TextView = itemView.findViewById(R.id.timeT)
            var btnT : Button = itemView.findViewById(R.id.btnT)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VIewHolder {
            var inflater = LayoutInflater.from(context)
            var myView : View = inflater.inflate(R.layout.item , parent , false)

            return VIewHolder(myView)
        }

        override fun getItemCount(): Int {
            return datalistI.size
        }

        override fun onBindViewHolder(holder: VIewHolder, position: Int) {

            holder.titleT.text = "\uD83D\uDCB0 Income"
            var dataIncome = datalistI[position]
            holder.reasonT.text = dataIncome.reason
            holder.amountT.text = dataIncome.amount.toString()

            val millis = dataIncome.time.toLong()
            val date = Date(millis)
            val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())
            val formattedTime = sdf.format(date)
            holder.timeT.text = formattedTime

            holder.btnT.setOnClickListener{
                dbsa.deleteIData(dataIncome.id.toString())

                Toast.makeText(context , "Data Deleted Successfully" , Toast.LENGTH_SHORT).show()

                onDeleteCallBack()
            }

        }

    }

    //=========================================================================================================================
    //=========================================================================================================================

    class MyAdapterExpense(var context: Context , var datalistE : ArrayList<DataClassExpense> , private val onDeleteCallBack : () -> Unit) : RecyclerView.Adapter<MyAdapterExpense.VIewHolderE>(){

        class VIewHolderE(itemView : View) : RecyclerView.ViewHolder(itemView){
            var titleT : TextView = itemView.findViewById(R.id.titleT)
            var reasonT : TextView = itemView.findViewById(R.id.reasonT)
            var amountT : TextView = itemView.findViewById(R.id.amountT)
            var timeT : TextView = itemView.findViewById(R.id.timeT)
            var btnT : Button = itemView.findViewById(R.id.btnT)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VIewHolderE {
            var inflater = LayoutInflater.from(context)
            var myView : View = inflater.inflate(R.layout.item , parent , false)

            return VIewHolderE(myView)
        }

        override fun getItemCount(): Int {
            return datalistE.size
        }

        override fun onBindViewHolder(holder: VIewHolderE, position: Int) {

            holder.titleT.text = "\uD83D\uDCB0 Expense"
            var dataExpense = datalistE[position]
            holder.reasonT.text = dataExpense.reason
            holder.amountT.text = dataExpense.amount.toString()

            val millis = dataExpense.time.toLong()
            val date = Date(millis)
            val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())
            val formattedTime = sdf.format(date)
            holder.timeT.text = formattedTime

            holder.btnT.setOnClickListener{

                dbsa.deleteEData(dataExpense.id.toString())

                Toast.makeText(context , "Data Deleted Successfully" , Toast.LENGTH_SHORT).show()

                onDeleteCallBack()

            }
        }
    }

}