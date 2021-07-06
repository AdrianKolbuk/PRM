package pl.pjatk.prm03

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import pl.pjatk.prm03.databinding.ActivityRegister2Binding
import pl.pjatk.prm03.databinding.ActivityRegisterBinding
import pl.pjatk.prm03.model.DatabaseModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase;
    private lateinit var dbReference: DatabaseReference
    private val binding by lazy{
        ActivityRegisterBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()
        dbReference = database.getReference("Users")

        binding.buttonRegister.setOnClickListener {
            registerUser(binding.root)
        }

    }

    fun openMainActivity(view: View){
        startActivity(Intent(this, MainActivity::class.java))
    }

    fun registerUser(view: View){
        if(binding.username.text.toString() == "" ||
            binding.email.text.toString() == "" ||
            binding.password.text.toString() == "" ||
            binding.confirmPassword.text.toString() == "")
        {
            Toast.makeText(this, "Please, fill empty fields", Toast.LENGTH_SHORT).show()
        }
        else if(binding.password.text == binding.confirmPassword.text)
        {
            Toast.makeText(this, "password and confirm password are not identical", Toast.LENGTH_SHORT).show()
        }
        else
        {
            var tmp = binding.email.text.toString().split(".")
            var email = tmp[0] + "%" + tmp[1]

            val databaseModel = DatabaseModel(
                binding.username.text.toString(),
                email,
                binding.password.text.toString()
            )
            dbReference.child(email).setValue(databaseModel)

            val mainActivityIntent = Intent(this, MainActivity::class.java)
            mainActivityIntent.putExtra("newAccount", 1)
            this.startActivity(mainActivityIntent)
            finish()
        }
    }
}