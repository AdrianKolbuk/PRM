package pl.pjatk.managerfinansowy

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import pl.pjatk.managerfinansowy.databinding.ActivityAddBinding
import pl.pjatk.managerfinansowy.model.Fund
import pl.pjatk.managerfinansowy.model.Shared
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate

class AddActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private val binding by lazy { ActivityAddBinding.inflate(layoutInflater) }
    var formatter: DateFormat = SimpleDateFormat("yyyy/MM/dd")

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val adapter = ArrayAdapter.createFromResource(
                this,
                R.array.options,
                android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.option.adapter = adapter
        binding.option.onItemSelectedListener = this

        setupSave()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setupSave() {
        binding.buttonSave.setOnClickListener() {
            val fund = Fund(
                    binding.place.text.toString(),
                    binding.amount.text.toString().toDouble(),
                    LocalDate.parse(binding.date.text.toString()),
                    binding.category.text.toString(),
                    binding.option.selectedItem.toString()
            )
            Shared.fundList.add(fund)

            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val text: String = parent?.getItemAtPosition(position).toString()
    }

}