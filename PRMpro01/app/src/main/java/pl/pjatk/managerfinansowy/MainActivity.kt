package pl.pjatk.managerfinansowy

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import pl.pjatk.managerfinansowy.adapter.FundAdapter
import pl.pjatk.managerfinansowy.databinding.ActivityMainBinding
import pl.pjatk.managerfinansowy.model.Shared
import java.time.LocalDate

private const val REQUEST_ADD_FUND = 1

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) } //utworzenie obiektu dopiero, gdy będziemy tego potrzebowali
    private val fundAdapter by lazy { FundAdapter(Shared.fundList) }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.addButton.setOnClickListener() { openAddActivity(binding.root) }
        setupFundList()
    }

    private fun setupFundList() {
        binding.financeList.apply {
            adapter = fundAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    fun openAddActivity(view: View) {
        startActivityForResult(
                Intent(this, AddActivity::class.java),
                REQUEST_ADD_FUND
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupTotalAmount() {
        var totalAmount = 0.0
        for(fund in Shared.fundList){
            if(fund.date.monthValue == LocalDate.now().monthValue && fund.date.year == LocalDate.now().year){
                if(fund.option == "Przychód")
                    totalAmount += fund.amount
                else
                    totalAmount -= fund.amount
            }
        }
        binding.total.text = "Saldo: $totalAmount"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_ADD_FUND && resultCode == Activity.RESULT_OK){
            fundAdapter.list = Shared.fundList
            fundAdapter.refresh()
            setupTotalAmount()
        } else super.onActivityResult(requestCode, resultCode, data)
    }


}