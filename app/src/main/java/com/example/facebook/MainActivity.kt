package com.example.facebook

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import com.example.facebook.db.DatabaseFb

class MainActivity : AppCompatActivity() {

    lateinit var sharedPreference : SharedPreferences
    lateinit var db : DatabaseFb
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
         db = DatabaseFb(this)
        sharedPreference = this.getSharedPreferences("etat_app", Context.MODE_PRIVATE)
        var estAuthentifier = sharedPreference.getBoolean("estIdentifier", false)
        if (estAuthentifier)
        {
            var email = sharedPreference.getString("email", "")
            var password = sharedPreference.getString("password", "")
            Intent(applicationContext, Acceuil::class.java).also {
                it.putExtra("email", email)
                it.putExtra("password", password)
                startActivity(it)
            }
        }else
        {
                Toast.makeText(applicationContext, "Connectez vous", Toast.LENGTH_SHORT).show()

        }

        val toolbar : Toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(Color.WHITE)
         val connecte : Button = findViewById(R.id.connecte) as Button
         val creeCompte : TextView = findViewById(R.id.creeCompte)
          connecte.setOnClickListener{
              val emailText : String = findViewById<TextView>(R.id.email).text.toString().trim()
              val passwordText : String = findViewById<TextView>(R.id.password).text.toString().trim()

                  val error : TextView = findViewById<TextView>(R.id.error)
                  if(db.findUser(emailText, passwordText) != null)
                  {
                      error.isVisible = false
                      // On memorise la connexion
//                      var poste = Poste()
                      var edit = sharedPreference.edit()
                      edit.putBoolean("estIdentifier", true)
                      edit.putString("email", emailText)
                      edit.putString("password", passwordText)
                      edit.commit() // ou apply()


//                      Toast.makeText(applicationContext, "Vous etes connecter", Toast.LENGTH_SHORT).show()
                         Intent(applicationContext, Acceuil::class.java).also {
                             it.putExtra("email", emailText)
                             it.putExtra("password", passwordText)
                             startActivity(it)
                         }

                  }else
                  {
                      error.isVisible = true

                  }



          }

        creeCompte.setOnClickListener {
            Intent(this, FormeUser::class.java).also {
                startActivity(it)
            }

        }

    }
}