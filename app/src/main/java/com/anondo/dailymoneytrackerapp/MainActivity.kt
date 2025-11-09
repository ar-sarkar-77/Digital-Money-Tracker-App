package com.anondo.dailymoneytrackerapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.transition.Visibility
import ir.mahozad.android.PieChart

class MainActivity : AppCompatActivity() {

    lateinit var TotalBalance : TextView
    lateinit var totalIncome : TextView
    lateinit var totalExpense : TextView
    lateinit var btnAddIncome : Button
    lateinit var btnAddExpense : Button
    lateinit var btnShowIncome : Button
    lateinit var btnShowExpense : Button
    lateinit var dbase : DatabaseOpenHelper
    lateinit var pieChart : PieChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        TotalBalance = findViewById(R.id.TotalBalance)
        totalIncome = findViewById(R.id.totalIncome)
        totalExpense = findViewById(R.id.totalExpense)
        btnAddIncome = findViewById(R.id.btnAddIncome)
        btnAddExpense = findViewById(R.id.btnAddExpense)
        btnShowIncome = findViewById(R.id.btnShowIncome)
        btnShowExpense = findViewById(R.id.btnShowExpense)
        pieChart = findViewById(R.id.pieChart)

        dbase = DatabaseOpenHelper(this)

        btnAddIncome.setOnClickListener{

            Add_Data.income = true
            startActivity(Intent(this , Add_Data::class.java))

        }

        btnAddExpense.setOnClickListener{

            Add_Data.income = false
            startActivity(Intent(this , Add_Data::class.java))

        }

        btnShowIncome.setOnClickListener{

            Show_Data.income = true
            startActivity(Intent(this , Show_Data::class.java))

        }

        btnShowExpense.setOnClickListener{

            Show_Data.income = false
            startActivity(Intent(this , Show_Data::class.java))

        }

        UpdateUI()


    }

    fun UpdateUI(){

        var totalIncomes = dbase.calculateIncome()
        var totalExpenses = dbase.calculateExpense()

        var totalBala = totalIncomes + totalExpenses

       if (totalBala>0){

           pieChart.visibility = View.VISIBLE

         if (totalIncomes>totalExpenses){

             var pieChartEx : Float = ((totalExpenses/totalIncomes)).toFloat()
             var pieChartIn : Float = 1-pieChartEx

             pieChart.slices = listOf(
                 PieChart.Slice(pieChartIn, Color.GREEN),
                 PieChart.Slice(pieChartEx, Color.RED)
             )

         }
           else if (totalIncomes==totalExpenses){
             pieChart.visibility = View.GONE
         }
           else{
               pieChart.visibility = View.GONE
         }




       }
        else{
            pieChart.visibility  = View.GONE
       }

        totalIncome.text = ""+totalIncomes

        totalExpense.text = ""+totalExpenses

        var totalBalancess = totalIncomes - totalExpenses

        if (totalBalancess<3000){

            TotalBalance.setTextColor(Color.YELLOW)

        }

        if (totalIncomes>totalExpenses){
            var totalBalaness = totalIncomes-totalExpenses
            TotalBalance.text = "BDT "+totalBalaness
            TotalBalance.setTextColor(Color.parseColor("#ffffff"))
        }
        else if (totalIncomes==totalExpenses){
            TotalBalance.text = "BDT 0.00"
            TotalBalance.setTextColor(Color.parseColor("#ffffff"))
        }
        else{
            var totalBalance = totalExpenses-totalIncomes
            TotalBalance.text = "BDT -"+totalBalance
            TotalBalance.setTextColor(Color.parseColor("#FF0404"))
        }

    }

    override fun onPostResume() {
        super.onPostResume()
        UpdateUI()
    }
}