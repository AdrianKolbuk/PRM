package pl.pjatk.prm03

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import pl.pjatk.prm03.databinding.ActivityMainBinding
import pl.pjatk.prm03.model.ItemNews
import pl.pjatk.prm03.shared.CurrentUser
import pl.pjatk.prm03.shared.News
import java.net.URL
import java.nio.charset.Charset


class MainActivity : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase;
    private lateinit var dbReference: DatabaseReference

    private val binding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()
        dbReference = database.getReference("Users")

        binding.buttonRegister.setOnClickListener {
            openRegisterActivity(binding.root)

        }

        binding.buttonLogin.setOnClickListener {
            login(binding.root)
        }
        if (Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }
        if (intent.getIntExtra("newAccount", 0) == 1) {
            Toast.makeText(this, "New account created successfully", Toast.LENGTH_SHORT).show()
        }

        getRSS()

    }

    fun openRegisterActivity(view: View){
        startActivity(Intent(this, RegisterActivity::class.java))
        finish()
    }

    fun login(view: View){
        var email = ""
        try{
            val tmp = binding.email.text.toString().split(".")
            email = tmp[0] + "%" + tmp[1]
        }catch(e: Exception){
            Toast.makeText(this, "Incorrect email", Toast.LENGTH_SHORT).show()
        }

        dbReference.child(email).get().addOnSuccessListener {
            if(it.exists() && it.value != null){
                if(binding.password.text.toString() == it.child("password").value.toString()){
                    CurrentUser.username = it.child("username").value.toString()
                    startActivity(Intent(this, NewsActivity::class.java))
                } else{
                    Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show()
                }
            } else{
                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getRSS(){
        Thread {
            val urlContent = URL("https://www.buzzfeed.com/tvandmovies.xml").readBytes().toString(Charset.defaultCharset())
            var tmp = urlContent.split("<item>").drop(1)
            println(urlContent)

            for(item in tmp){
                try{
                    val title = item.split("<title>")[1].split("</title>")[0]
                    val description = item.split("<description><![CDATA[")[1].split("]]></description>")[0]
                    val link = item.split("<link>")[1].split("</link>")[0]
                    val imgUrl = item.split("<media:thumbnail url=\"")[1].split("\"")[0]

                    val itemNews = ItemNews(
                            title,
                            description,
                            link,
                            BitmapFactory.decodeStream(URL(imgUrl).openConnection().getInputStream()),
                            link,
                            false
                    )
                    println(itemNews.toString())
                    News.newsList.add(itemNews)
                }catch(e: Exception){
                    println("news unavaible")
                }
            }
        }.start()
    }



}